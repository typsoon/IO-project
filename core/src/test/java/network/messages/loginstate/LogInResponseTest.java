package network.messages.loginstate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.messages.utils.DataConsumer;
import network.messages.utils.OutputStreamDataReceiver;

public class LogInResponseTest {
    private LogInResponse testedResponse;
    private ByteArrayOutputStream out;
    private DataConsumer dataReceiver;

    @BeforeEach
    void init() {
        out = new ByteArrayOutputStream();
        dataReceiver = new OutputStreamDataReceiver(out);
    }

    @Test
    void payloadWithTokenIsCorrectlyEncoded() throws IOException {
        testedResponse = new LogInResponse(CorrectlyEncodedPayload.payloadWithToken, CorrectlyEncodedPayload.userId);
        testedResponse.encodeAndWrite(dataReceiver);

        assertArrayEquals(CorrectlyEncodedPayload.encodedPayloadWithToken, out.toByteArray());
    }

    @Test
    void payloadWithoutTokenIsCorrectlyEncoded() throws IOException {
        testedResponse = new LogInResponse(CorrectlyEncodedPayload.noTokenPayload, 0);
        testedResponse.encodeAndWrite(dataReceiver);

        assertArrayEquals(CorrectlyEncodedPayload.encodedNoTokenPayload, out.toByteArray());
    }

}

class CorrectlyEncodedPayload {
    static final int NO_AUTH_TOKEN_INDICATOR = -1;
    static final int msgCode = 1;

    static final Optional<Integer> payloadWithToken = Optional.of(42);
    static final int userId = 8;
    static final byte[] encodedPayloadWithToken;

    static {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);

        try {
            out.write(Byte.BYTES + Integer.BYTES);
            out.write(msgCode);
            out.writeInt(payloadWithToken.get());
            out.writeInt(userId);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        encodedPayloadWithToken = byteOut.toByteArray();
    }

    static final Optional<Integer> noTokenPayload = Optional.empty();
    static final byte[] encodedNoTokenPayload;

    static {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);

        try {
            out.write(Byte.BYTES + Integer.BYTES);
            out.write(msgCode);
            out.writeInt(NO_AUTH_TOKEN_INDICATOR);
            out.writeInt(0);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        encodedNoTokenPayload = byteOut.toByteArray();
    }
}
