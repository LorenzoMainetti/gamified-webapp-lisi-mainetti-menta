package it.polimi.db2.auxiliary;

import java.io.Serializable;

public enum UserStatus implements Serializable {
    BANNED, COMPLETED, NOT_COMPLETED, NOT_AVAILABLE;
}
