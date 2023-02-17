package operation;

import javax.management.JMException;
import java.util.HashMap;

public class SrtOperatorFactory {
    public SrtOperatorFactory() {
    }

//    ��ȡ��Ӧ srt �ļ���������
    public static SrtOperator getSrtOperator(String name, HashMap parameters) throws JMException {
        //ʱ����ƽ��
        if (name.equalsIgnoreCase("SrtTimeShift")) {
            return new SrtTimeShift(parameters);
        }
        //�ڵ���������Ӧʱ�����Ļ��䣩
        else if (name.equalsIgnoreCase("SrtNodeSearch")) {
            return new SrtNodeSearch(parameters);
        }
        else {
            System.out.println("SrtOperatorFactory.getSrtOperator. Operator " + name + " not found ");
            throw new JMException("Exception in " + name + ".getSrtOperator()");
        }
    }
}
