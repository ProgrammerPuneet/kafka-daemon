package kafka.demo.kafkademo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KafkaDemoApplication {
	


	public static void main(String[] args) {
		//System.out.println(Objects.nonNull("4567")&&null);
		//String id="ewewewe-wefrwjebjfk";
		//log.info("Request Identifier for PreActivatePurchaseOrderApi = {}",id);
//		int p=(2==3)?solve(1):solve(2);
//		Map<String,Integer>m=new HashMap<String,Integer>();
//		m.put("cactus", 3);
//		//Integer p=m.get("cactusi");
//		System.out.println(p);
		Map<String, Integer> dcTypeVsDailyCap=new HashMap<String, Integer>();
		dcTypeVsDailyCap.put("RDC", 12);
		try {
			int p=dcTypeVsDailyCap.getOrDefault("GDC",4);
			System.out.println("hello "+p);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		List<String>list=new ArrayList<>();
		//Double c=null;
		//System.out.println(((Double)c).intValue());
		//System.out.println(ParameterizedTypeReference.forType(ResolvableType.forClass(KafkaDemoApplication.class).getType()));
		
	}
	public static int solve(int p) {
		return 1+p;
	}

}
