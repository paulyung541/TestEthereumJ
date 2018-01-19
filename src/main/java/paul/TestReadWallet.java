package paul;

import java.util.Map;

public class TestReadWallet {
    public static void main(String[] args) {

//        Account account = ETCWalletUtils.getInstance().loadWalletFromFile("D:\\3bodyETH\\testetc", "123");
//        System.out.println(String.format("钱包地址: %s, 钱包私钥: %s", Hex.toHexString(account.getAddress()), Hex.toHexString(account.getEcKey().getPrivKeyBytes())));

        /*余额查询方式一，根据本地区块信息查询*/
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CommonConfig.class, DefaultConfig.class);
//        account.setRepository(context.getBean(RepositoryWrapper.class));
//        BigInteger balance = account.getBalance();
//        System.out.println("ETC余额: " + balance);
//        System.exit(0);

        /*余额查询方式二，根据第三方服务器查询*/
//        Hex.toHexString(account.getAddress())
        Map<String, String> balacesMsg = ETCWalletUtils.getInstance().getBalance("0x7A3fE333c3F811aF9B5777a48750A4500268c2D6");
        System.out.println(String.format("余额：%s ETC", balacesMsg.get("ETC")));
    }
}
