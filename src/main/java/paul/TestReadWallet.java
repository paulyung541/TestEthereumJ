package paul;

import org.ethereum.core.Account;
import org.spongycastle.util.encoders.Hex;

public class TestReadWallet {
    public static void main(String[] args) {
        Account account = ETCWalletUtils.getInstance().loadWalletFromFile("D:\\3bodyETH\\testetc", "123");
        System.out.println(String.format("钱包地址: %s, 钱包私钥: %s", Hex.toHexString(account.getAddress()), Hex.toHexString(account.getEcKey().getPrivKeyBytes())));
    }
}
