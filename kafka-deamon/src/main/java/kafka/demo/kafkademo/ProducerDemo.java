package kafka.demo.kafkademo;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

public class ProducerDemo {
    public void sendMessage(){
        String url="kafka-1301504412-1-1334708561.scus.kafka-v2-atlas-secured-stg.ms-df-messaging.stg-az-southcentralus-10.prod.us.walmart.net:9093,kafka-1301504412-2-1334708564.scus.kafka-v2-atlas-secured-stg.ms-df-messaging.stg-az-southcentralus-10.prod.us.walmart.net:9093,kafka-1301504412-3-1334708567.scus.kafka-v2-atlas-secured-stg.ms-df-messaging.stg-az-southcentralus-10.prod.us.walmart.net:9093"; 
        System.out.println("initializing secure kafka producer");
        
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        String filePath = "file:///etc/secrets/gls-atlas-fixit-kafka.prod.walmart.com.jks";

        System.out.println("initializing secure kafka producer");
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.RETRIES_CONFIG, 2);
		properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
		properties.put("enable.idempotence" , "false");
		System.out.println("Loading jks file: " + filePath);
		properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, filePath);
		properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "Walmart@1234");
		properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, filePath);
		properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,  "Walmart@1234");
		@SuppressWarnings("unchecked")
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer(properties);
        
        
        String auditItemUpdateTopic = "ATLAS_FPE_EVENTS_STG";
        String key= "US-KafkaValidationMessage";

        String invAdjMessage = "validation vghcfghcfgrfjytgfggkuygsuccess";
    
            Headers headers = new RecordHeaders();
            headers.add(new RecordHeader("facilityNum", String.valueOf("99999").getBytes()));
            headers.add(new RecordHeader("facilityCountryCode", "US".getBytes()));
            ProducerRecord<String,String> record=new ProducerRecord<String,String>(auditItemUpdateTopic,0,key,invAdjMessage,headers);
            kafkaProducer.send(record,new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // If Exception is null, the record was sent successfully
                    if(exception==null){
                        System.out.println("Message sent successfully");
                    }
                    else{
                        System.out.println("Error Occured "+exception);
                    }
                }
            });
            kafkaProducer.close();
            System.out.println("Sent message successfully");
      

    }
    
    
    
    public String getFile(String fileName) throws IOException {
        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();
        // System.out.println("Contents : " + fileAsString);
        buf.close();
        return fileAsString;
    }

}
