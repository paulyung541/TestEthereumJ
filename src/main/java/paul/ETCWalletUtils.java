package paul;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ETCWalletUtils {
    private static ETCWalletUtils _instance;

    public static ETCWalletUtils getInstance() {
        if (_instance == null) {
            synchronized (ETCWalletUtils.class) {
                if (_instance == null) {
                    _instance = new ETCWalletUtils();
                }
            }
        }
        return _instance;
    }

    /**
     * 生成ETC钱包并存储到文件
     *
     * @param walletPath 生成的钱包json文件目录
     * @param passwd     助记密码
     */
    public void newWallet2File(String walletPath, String passwd) {
        newWallet2File(walletPath, null, passwd);
    }

    /**
     * 生成ETC钱包并存储到文件
     *
     * @param walletPath      生成的钱包json文件目录
     * @param privKeyFileName 私钥key存储文件名称
     * @param passwd          助记密码
     */
    public void newWallet2File(String walletPath, String privKeyFileName, String passwd) {
        Account account = new Account();
        account.init();
        FileKeystore fileKeystore = new FileKeystore(walletPath);
        fileKeystore.storeKey(account.getEcKey(), passwd);

        if (privKeyFileName == null)
            return;
        File privKeyfile = new File(privKeyFileName);
        if (!privKeyfile.exists()) {
            try {
                privKeyfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(privKeyfile);
            out.write(Hex.toHexString(account.getEcKey().getPrivKeyBytes()).getBytes("utf-8"));
        } catch (Exception e) {
            fileKeystore.removeKey(Hex.toHexString(account.getAddress()));
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件中的钱包
     *
     * @param walletPath 钱包文件路径
     * @param passwd     助记密码
     * @return
     */
    public Account loadWalletFromFile(String walletPath, String passwd) {
        FileKeystore fileKeystore = new FileKeystore(walletPath);
        String[] strings = fileKeystore.listStoredKeys();
        if (strings.length == 0)
            throw new RuntimeException(walletPath + " 目录不存在钱包文件");
        ECKey ecKey = fileKeystore.loadStoredKey(strings[0].substring(2), passwd);
        if (ecKey == null)
            throw new RuntimeException("地址或密码错误");
        Account account = new Account();
        account.init(ecKey);
        return account;
    }

    public Map<String, String> getBalance(byte[] address) {
        return getBalance("0x" + Hex.toHexString(address));
    }

    /**
     * 根据地址信息去 https://www.etherchain.org 查询 ETC 余额
     *
     * @param addressHexString 地址16进制字符串
     * @return 返回 Map，根据 key 为 ETC 获取
     */
    public Map<String, String> getBalance(String addressHexString) {
        if (!addressHexString.contains("0x"))
            addressHexString = "0x" + addressHexString;

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url("https://www.etherchain.org/account/" + addressHexString)
                .build();

        Map<String, String> balanceMsg = new HashMap<>(2);
        try {
            Response response = client.newCall(request).execute();
            String html = response.body().string();
            StringBuilder builderEther = new StringBuilder();

            int index = html.indexOf(" ETH</p>");
            for (int i = index - 1; "0123456789.".contains("" + html.charAt(i)); i--)
                builderEther.insert(0, html.charAt(i));
            balanceMsg.put("ETC", builderEther.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return balanceMsg;
    }
}
