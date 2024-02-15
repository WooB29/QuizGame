package Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class AES {
    //키 (32byte)
    private final String aesKey = "abcdefghabcdefghabcdefghabcdefgh";

    // 초기화 벡터 (16byte)
    private final String aesIv = "0123456789abcdef";

    public String encrypt_AES(String id){
        // AES 암호화
        try {
            String result; // 암호화 결과 값을 담을 변수 선언

            // AES/CBC/PKCS5Padding -> AES, CBC operation mode, PKCS5 padding scheme 으로 초기화된 Cipher 객체
            // 암호화 기능이 포함된 객체 생성
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // 미리 선언한 키로(32byte) 비밀키 생성
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");

            // 미리 선언한 iv(16byte) 로 spec 생성
            // 매번 다른 IV를 생성하면 같은 평문이라도 다른 암호문을 생성할 수 있다.
            IvParameterSpec ivParamSpec = new IvParameterSpec(aesIv.getBytes());

            // 암호화 적용
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

            // 암호화 실행
            // ID 암호화(인코딩 설정)
            byte[] encrypted = cipher.doFinal(id.getBytes(StandardCharsets.UTF_8));

            // 암호화 인코딩 후 저장
            result = Base64.getEncoder().encodeToString(encrypted);

            // 암호화된 결과값 리턴
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
