package it.polimi.db2.auxiliary;

import java.io.Serializable;

/**
 * Used for admin homepage content, this avoid sending 'null' and it's used client side to check
 * if the product is there or not.
 */
public enum AdminStatus implements Serializable {
    AVAILABLE ,NOT_AVAILABLE;
}
