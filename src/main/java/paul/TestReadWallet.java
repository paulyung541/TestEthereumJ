package paul;

import org.spongycastle.util.encoders.Hex;

import java.util.Map;

public class TestReadWallet {
    public static void main(String[] args) {

        Account account = ETCWalletUtils.getInstance().loadWalletFromFile("D:\\3bodyETH\\testetc", "123");
        System.out.println(String.format("钱包地址: %s, 钱包私钥: %s", Hex.toHexString(account.getAddress()), Hex.toHexString(account.getEcKey().getPrivKeyBytes())));

        /*余额查询方式一，根据本地区块信息查询*/
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CommonConfig.class, DefaultConfig.class);
//        account.setRepository(context.getBean(RepositoryWrapper.class));
//        BigInteger balance = account.getBalance();
//        System.out.println("ETC余额: " + balance);
//        System.exit(0);

        /*余额查询方式二，根据第三方服务器查询*/
        Map<String, String> balacesMsg = ETCWalletUtils.getInstance().getBalance(Hex.toHexString(account.getAddress()));
        System.out.println(String.format("余额：%s ether, %s wei", balacesMsg.get("Ether"), balacesMsg.get("Wei")));
    }
}
