package com.topotek.module.project.commandAnalyzer;

//执行和分发命令
public class CommandAnalyzer {

    //最终接收到串口传回来的命令  参数command
    public static void analyzeCommand(String command, CommandExecuter commandExecuter) {

        //有可能是混乱电压 发了一个空命令
        if (command == null)
            return;

        int length = command.length();

        //如果长度是6那么按照之前的协议 :wKey1
        if (length == 6)
            analyzeOldCommand(command, commandExecuter);
        else if (length > 6)
            //大于6按照新的协议来做
            analyzeNewCommand(command, length, commandExecuter);
    }

    //  #   T   P   源   去   数   w   A   W   B   0   1   R
    //  0   1   2   3    4    5    6   7   8   9   10  11  12
    //第三个 参数是帮助执行命令的接口
    private static void analyzeNewCommand(String command, int length, CommandExecuter commandExecuter) {
        switch (command.substring(0, 3)) {
            case "#TP":
            case "#tp":
                break;
            default:
                return;
        }
        //命令的第三位是 代表现在是哪个地方发过来的命令 数据源
        switch (command.charAt(3)) {
            case 'U'://Uart命令
            case 'M'://M3
            case 'D'://显示APP
            case 'I'://算法APP
            case 'E'://外部接口UART
                break;
            default:
                return;
        }

      //charAt(4)代表命令是发给谁的   如果命令不是给相机就return
        if (command.charAt(4) != 'D')
            return;

        //代表一个16位的字符 指的是后面的数据???
        char hex_num = command.charAt(5);

        //将charAt(5)的字符转换为int类型的数据
        int num;
        if (hex_num >= '2' && hex_num <= '9')
            num = hex_num - '0';
        else if (hex_num >= 'A' && hex_num <= 'F')
            num = hex_num - 'A' + 10;
        else return;

        //如果不对代表数据不正确
        if (length != 12 + num)
            return;

        //截取类型
        String type = command.substring(6, 10);
        //截取参数
        String parameter = command.substring(10, 10 + num);

        analyzeNewCommand(type, parameter, num, commandExecuter);
    }

    //  #   T   P   源   去   数   w   A   W   B   0   1   R   R
    //  0   1   2   3    4    5    6   7   8   9   10  11  12  13

    private static void analyzeNewCommand(String type, String parameter, int parameterLength, CommandExecuter commandExecuter) {

        if(parameterLength == 2){
            switch (type) {
                case "rSDC":
                    commandExecuter.rSDC();
                    break;
                case "rPIP":
                    commandExecuter.rPIP();
                    break;
                case "rREC":
                    commandExecuter.rREC();
                    break;
                case "rAWB":
                    commandExecuter.rAWB();
                    break;
                case "rISO":
                    commandExecuter.rISO();
                    break;
                case "rEVS":
                    commandExecuter.rEVS();
                    break;
                case "rPIC":
                    commandExecuter.rPIC();
                    break;
                case "rVID":
                    commandExecuter.rVID();
                    break;
                case "wTRC":
                    commandExecuter.wTRC();
                    break;
                case "wPIP":
                    commandExecuter.wPIP(parameter.charAt(1));
                    break;

                case "wFUS":
                    commandExecuter.wFUS(parameter.charAt(1));
                    break;

                case "wAWB":
                    commandExecuter.wAWB(parameter.toCharArray());
                    break;
                case "wISO":
                    commandExecuter.wISO(parameter.toCharArray());
                    break;
                case "wEVS":
                    commandExecuter.wEVS(parameter.toCharArray());
                    break;
                case "wPIC":
                    commandExecuter.wPIC(parameter.toCharArray());
                    break;
                case "wVID":
                    commandExecuter.wVID(parameter.toCharArray());
                    break;
                case "wCAP":
                    commandExecuter.wCAP(parameter.toCharArray());
                    break;
                case "wREC":
                    commandExecuter.wREC(parameter.toCharArray());
                    break;
                case "wDIS":
                    commandExecuter.wDIS(parameter.toCharArray());
                    break;

                case "wIMG":
                    commandExecuter.wIMG(parameter.toCharArray());
                    break;
                    //添加一个新命令或者新功能
/*
                case "XXX":
                    commandExecuter.wXXX(parameter);*/
            }
        }else {
            switch (type) {
                case "wZMP"://3wZMPxxx                                  //7wZMPxxxzzzz
                    if(parameterLength >= 3) //180    18.0×            //1801000    18.0x  zoom:1000
                        commandExecuter.wZMP(parameter);
                    break;
                case "wTIM"://FwTIMHHmmss.ssyyMMdd
                    if(parameterLength == 0xF) //112430.30171114    2017-11-14 11:24:30.30
                        commandExecuter.wTIM(parameter);
                    break;
                case "wLAT"://CwLATXAAAMM.MMMMM    X: W/E
                    if(parameterLength == 0xC) //E11617.20478    E116°17.20478'
                        commandExecuter.wLAT(parameter);
                    break;
                case "wLON"://BwLONXAAMM.MMMMM    X: S/N
                    if(parameterLength == 0xB) //N4006.88395    N40°06.88395'
                        commandExecuter.wLON(parameter);
                    break;
                case "wLRF"://7wLRFxxx.xxx or 7wLRFxxxxx.x
                    if(parameterLength == 7)//000.344    0.344米
                        commandExecuter.wLRF(parameter);
                    break;

//                case "wHIT"://AwHIT:wF716 X3 X2 X1 X0
//                    if(parameterLength == 0xA)//X3 X2 X1 X0    zoom位置
//                        commandExecuter.wHIT(parameter);
//                    break;
            }
        }
    }

    private static void analyzeOldCommand(String command, CommandExecuter commandExecuter) {
        switch (command.substring(0, 5)) {
            case ":wKey":
                commandExecuter.wKey(command.charAt(5));
                break;
            case ":wRec":
                commandExecuter.wRec(command.charAt(5));
                break;
        }
    }

    //命令接口 根据命令数据 调用不同的方法
    public interface CommandExecuter {

        void rSDC();

        void rPIP();

        void rREC();

        void rAWB();

        void rEVS();

        void rISO();

        void rPIC();

        void rVID();

        void wPIP(char parameter);

        void wAWB(char[] parameter);

        void wISO(char[] parameter);

        void wEVS(char[] parameter);

        void wPIC(char[] parameter);

        void wVID(char[] parameter);

        void wCAP(char[] parameter);

        void wREC(char[] parameter);

        void wDIS(char[] parameter);

        void wZMP(String parameter);

        void wTIM(String parameter);

        void wLAT(String parameter);

        void wLON(String parameter);

        void wLRF(String parameter);

        void wTRC();

        void wKey(char parameter);

        void wRec(char parameter);

        void wIMG(char[] parameter);

        void wFUS(char parameter);

        //添加接口
       /* void wXXX(String s);*/
    }
}
