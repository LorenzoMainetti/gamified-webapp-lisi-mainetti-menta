package it.polimi.db2.servlets.admin;

import it.polimi.db2.services.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static it.polimi.db2.auxiliary.images.ImageProcessor.*;

@WebServlet("/UploadImage")
@MultipartConfig
public class UploadImage extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    private final static int FILE_SIZE = 2000 * 2000;

    /**
     * Method to read the image from an input stream and convert it into a byte array
     *
     * @param imageInputStream input stream
     * @return byte array of the image
     * @throws IOException exception if there is a problem reading from the input stream
     */
    public static byte[] readImage(InputStream imageInputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[FILE_SIZE];// image can be maximum of 4MB
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

    /**
     * Method to send error in case of bad request
     *
     * @param error    type of error generated
     * @param response response
     * @throws IOException exception if there is a problem reading with the stream
     */
    private void sendBackMessage(String error, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(error);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream imgStream = request.getPart("image").getInputStream();

        boolean defaultImg = false;
        byte[] file;
        if (imgStream.available() == 0) {
            file = readImage(getClass().getClassLoader().getResource("not_found.png").openStream());
        } else {
            file = readImage(imgStream);
        }

        if (file.length > FILE_SIZE) {
            defaultImg = true;
            file = readImage(getClass().getClassLoader().getResource("not_found.png").openStream());
        } else {
            try {
                //try conversion
                switch (getImageType(file)) {
                    case "image/png":
                        //convert to jpeg
                        file = toByteArray(prepareImage(file, true), "jpg");
                        break;
                    case "image/jpeg":
                        file = toByteArray(prepareImage(file, false), "jpg");
                        break;
                    default:
                        file = readImage(getClass().getClassLoader().getResource("not_found.png").openStream());
                        defaultImg = true;
                        break;
                }
            } catch (NullPointerException e) {
                //if there are problems use the default image
                file = readImage(getClass().getClassLoader().getResource("not_found.png").openStream());
                defaultImg = true;
            }
        }

        int productId = (int) request.getSession().getAttribute("productId");
        productService.imageLoad(productId, file);
        request.getSession().removeAttribute("productId");

        response.setStatus(HttpServletResponse.SC_OK);
        if (defaultImg) sendBackMessage("Product Created but with a default image because uploaded file is not supported", response);
        else sendBackMessage("Product Successfully Created!", response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
