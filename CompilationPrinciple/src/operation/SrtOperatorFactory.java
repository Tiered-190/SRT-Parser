package operation;

import javax.management.JMException;
import java.util.HashMap;

public class SrtOperatorFactory {
    public SrtOperatorFactory() {
    }

//    获取对应 srt 文件操作功能
    public static SrtOperator getSrtOperator(String name, HashMap parameters) throws JMException {
        //时间轴平移
        if (name.equalsIgnoreCase("SrtTimeShift")) {
            return new SrtTimeShift(parameters);
        }
        //节点搜索（对应时间的字幕语句）
        else if (name.equalsIgnoreCase("SrtNodeSearch")) {
            return new SrtNodeSearch(parameters);
        }
        else {
            System.out.println("SrtOperatorFactory.getSrtOperator. Operator " + name + " not found ");
            throw new JMException("Exception in " + name + ".getSrtOperator()");
        }
    }
}
