View仅仅负责实现单纯的、独立的UI操作，尽量不要去维护数据（View层指Activity、Fragment这类层级）

Model负责处理数据请求、业务逻辑，不涉及UI操作

Presenter是MVP体系的控制中心，负责给View和Model安排工作 ，什么时候调用Model处理逻辑，什么时候调用View反应结果，都是Presenter说了算

View与Model均以接口的形式出现在Presenter中，Presenter通过调用 View与Model的实现接口，来操作 View与Model；同时Presenter也是以接口的形式出现在View中，这样Presenter与View就是通过接口相互依赖了

Presenter是主动方，View是被动方，对于绑定到View上的数据，不是View调用Presenter主动拉取数据，而是Presenter主动将数据推给View


DevMvp
    ├─api//URL、接口管理、网络请求封装类
    ├─mvp//项目主体
    │  ├─base//基础类封装
	    ├─bean//实体类
    │  ├─contract//契约类 用于统一管理view和presenter的接口
    │  ├─model//M层-数据处理
    │  ├─presenter//P层-逻辑业务处理
    │  └─view//V层-页面渲染
    │      ├─activity
		    ├─adapter
    │      └─fragment
		    ...//类似Dialog、PopupWindow也可以放在view下
    └─utils//工具类
        └─rxhelper//Rx封装工具





密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: androiddebugkey
创建日期: 2019-4-15
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: C=US, O=Android, CN=Android Debug
发布者: C=US, O=Android, CN=Android Debug
序列号: 1
有效期开始日期: Mon Apr 15 18:56:33 CST 2019, 截止日期: Wed Apr 07 18:56:33 CST 2049
证书指纹:
         MD5: E1:15:1D:A1:F0:48:7D:D0:E9:E4:9B:F9:33:B5:4B:E3
         SHA1: DC:68:01:97:8F:6A:B9:10:CC:CC:E1:E3:FF:CF:E7:FF:5A:2F:28:4F
         SHA256: 34:60:45:6C:89:F9:E6:9F:4D:E4:99:0B:AE:EF:00:42:47:CE:77:9E:95:64:6D:E6:85:12:3B:C8:4F:78:74:CA
         签名算法名称: SHA1withRSA
         版本: 1








别名: mappjks
创建日期: 2019-5-24
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=mapp, OU=sinovatio, O=sinovatio, L=nanjing, ST=jiangsu, C=86
发布者: CN=mapp, OU=sinovatio, O=sinovatio, L=nanjing, ST=jiangsu, C=86
序列号: 41568498
有效期开始日期: Fri May 24 10:12:59 CST 2019, 截止日期: Tue May 17 10:12:59 CST 2044
证书指纹:
         MD5: FA:5F:89:B7:08:03:11:7E:DE:10:E6:C5:9E:CB:AF:31
         SHA1: CA:61:22:2F:47:88:36:85:C1:96:36:C5:18:9B:AE:8A:8E:37:60:0F
         SHA256: 40:83:DB:85:4C:DE:2A:9C:8C:BC:66:45:16:4C:2C:09:42:1C:CC:93:E0:A0:0C:80:12:DC:1F:D7:9D:D5:E2:18
         签名算法名称: SHA256withRSA
         版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: FD 9D 87 3E B5 33 3E 39   90 9F DC 38 30 2C 68 EF  ...>.3>9...80,h.
0010: C2 90 91 2B                                        ...+
]
]
