package server;

import annotations.Rfc;

/**
 * Created by isaac on 2/8/16.
 */
@Rfc("https://tools.ietf.org/html/rfc7230#section-2.6")
public class HttpVersion {
    public static final HttpVersion HTTP_V_1_1 = new HttpVersion("HTTP/1.1");
    private int majorVersion;
    private int minorVersion;

    public HttpVersion(String httpVersion) {
        int majorVersionIndex = httpVersion.indexOf('/') + 1;
        int minorVersionIndex = httpVersion.indexOf('.') + 1;

        char majorVersionChar = httpVersion.charAt(majorVersionIndex);
        char minorVersionChar = httpVersion.charAt(minorVersionIndex);

        majorVersion = Character.getNumericValue(majorVersionChar);
        minorVersion = Character.getNumericValue(minorVersionChar);
    }

    @Override
    public String toString() {
        return String.format("HTTP/%d.%d", majorVersion, minorVersion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpVersion that = (HttpVersion) o;

        if (majorVersion != that.majorVersion) return false;
        return minorVersion == that.minorVersion;

    }

    @Override
    public int hashCode() {
        int result = majorVersion;
        result = 31 * result + minorVersion;
        return result;
    }

}
