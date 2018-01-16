package paul;

import org.ethereum.core.Account;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
            out.write(account.getEcKey().getPrivKeyBytes());
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
}
