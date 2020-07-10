package epam.eremenko.restaurant.service.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class Encoder {
    private Encoder() {
    }

    public static String md5Apache(String st) {
        return DigestUtils.md5Hex(st);
    }
}
