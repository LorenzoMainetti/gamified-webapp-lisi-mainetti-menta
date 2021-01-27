package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Product;
import it.polimi.db2.services.ProductService;
import jakarta.annotation.Resources;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;

import static it.polimi.db2.auxiliary.images.ImageProcessor.*;
import static java.nio.file.Files.readAllBytes;


@WebServlet("/UploadImage")
@MultipartConfig
public class UploadImage extends HttpServlet {

    public final static int FILE_SIZE = 4096*100;

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    public static byte[] readImage(InputStream imageInputStream) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];// image can be maximum of 4MB
        int bytesRead = -1;

        try {
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw e;
        }

    }

    private void sendBackError(String error, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println(error);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InputStream imgStream = request.getPart("image").getInputStream();
        byte[] file;

        if(!(imgStream.available() >0)) {
            //pick a default one
            File f  = new File("http://localhost:8989/db2_lisi_mainetti_menta_war_exploded/images/test.png");
            file = Files.readAllBytes(f.toPath());
        }
        else {
            file = readImage(imgStream);
        }


        if (file.length > FILE_SIZE) {
            sendBackError("file is too large. It would probably jeopardize the whole web application", response);
            return;
        }

        //force it to jpg if it's png, then everything goes into a byte array variable
        switch (getImageType(file)) {
            case "image/png":
                //convert to jpeg
                file = toByteArray(prepareImage(file, true), "jpg");
                break;

            case "image/jpeg":
                file = toByteArray(prepareImage(file, false), "jpg");
                break;
            default:
                sendBackError("format not supported. It would probably jeopardize the whole web application", response);
                return;

        }


        int productId = (int) request.getSession().getAttribute("productId");
        //Product product = productService.getProductOfTheDay();
        productService.dummyImageLoad(productId, file);
        request.getSession().removeAttribute("productId");

        response.setStatus(HttpServletResponse.SC_OK);



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
