package paul;

public class TestWriteWallet {
    public static void main(String[] args) {
        //生成钱包
        ETCWalletUtils.getInstance().newWallet2File("D:\\3bodyETH\\testetc", "D:\\3bodyETH\\testetc\\my-privkey.txt","123");
    }
}
