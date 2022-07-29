package kafka.demo.kafkademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExecuteShellComandProcessBuilder {
	public static void main(String[] args) throws IOException, InterruptedException, AuthenticationException,
			KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		List<String> listOfDeliveryNumbers = new ArrayList<String>();
		List<String> listOfExceptionIds = new ArrayList<String>();

		// STEP-1
		//////////////////////////////////////////////////////////////////////////////
		////////////// GETTING ALL THE DELIVERY NUMBERS FROM THE EXCEL////////////
		//////////////////////////////////////////////////////////////////////////////
		FileInputStream file = new FileInputStream(new File("/Users/p0t02g2/Desktop/DNumber.xlsx"));//add your file path
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		for (int i = 1; i < 1606; i++) {
			String k1 = formatter.formatCellValue(sheet.getRow(i).getCell(0));// number
			listOfDeliveryNumbers.add(k1);
		}

		// STEP-2
		//////////////////////////////////////////////////////////////////////////////
		////////////// FIND EXCEPTION ID FROM DELIVERY NUMBER///////////////////////
		//////////////////////////////////////////////////////////////////////////////
		HttpClient client = HttpClients.custom()
				.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		HttpPost httpPost1 = new HttpPost("https://api.fixit.platform.prod.us.walmart.net/graphql");

		for (String x : listOfDeliveryNumbers) {
			String json = "{\"query\":\"query SearchInspection(\\n  $searchInput: ExceptionSearchInput!\\n  $pageSize: Int!\\n  $pageContinuationToken: String\\n  $sortBy: String\\n  $sortDirection: SortDirection\\n) {\\n  searchException(\\n    searchInput: $searchInput\\n    pageSize: $pageSize\\n    pageContinuationToken: $pageContinuationToken\\n    sortBy: $sortBy\\n    sortDirection: $sortDirection\\n  ) {\\n    issues {\\n      exceptionId\\n      identifier\\n      dcNumber\\n      countryCode\\n      sellerType\\n      exceptionCategory\\n      exceptionSubCategory\\n      status\\n      statusReason\\n      createdBy\\n      createdOn\\n      modifiedBy\\n      modifiedOn\\n      assignment {\\n        assignedUser\\n      }\\n      delivery {\\n        id\\n        number\\n        scac\\n        trailerNumber\\n        load\\n        additionalInfo\\n        frieghtOrigin\\n        frieghtOriginCenter\\n      }\\n      cases{\\n        id\\n        label\\n        qty\\n        remainingQty\\n        slot\\n      }\\n      items {\\n        id\\n        itemNumber\\n        itemDescription\\n        departmentNumber\\n        itemUpc\\n        vendorName\\n        vendorNumber\\n        vendorStockNumber\\n        vnpkCost\\n        whpkCost\\n        vnpkQty\\n        whpkQty\\n        vnpk\\n        whpk\\n        receivedVnpkQty\\n        receivedWhpkQty\\n        qtyUom\\n        hazmat\\n        sellByDate\\n      }\\n     purchaseOrders {\\n        id\\n        event\\n        poType\\n        poNumber\\n        casePoNumber\\n        bookingPONumber\\n        poStatus\\n        poChannel\\n        poLineNumber\\n        poLineStatus\\n        orderedQty\\n        qtyUom\\n        receivedQtyTillDate\\n        shipDate\\n        cancelDate\\n        mabd\\n        partnerId\\n        additionalInfo\\n      }\\n      vendor {\\n        id\\n        vendorName\\n        vendorNumber\\n        vendorDeptNumber\\n      }\\n      details {\\n        id\\n        inspectionQty\\n        exceptionQty\\n        remainingQty\\n        exceptionUOM\\n        referenceTicket\\n        exceptionType\\n        businessStatus\\n        exemption\\n      }\\n      seller {\\n        id\\n        sellerName\\n      }\\n      defects {\\n        defectId\\n        type\\n        subType\\n        defectQty\\n        updatedOn\\n      }\\n      defectDetail {\\n        id\\n        inspectionQty\\n        noDefectQty\\n        defectQty\\n        type\\n        inspectionId\\n        inspectionRecordId\\n        inspectionRecordDisplayId\\n        inspectionRecordCategory\\n        qtyUom\\n        exemption\\n      }\\n      inspectionRecordDetail {\\n        id\\n        inspectionQty\\n        noDefectQty\\n        inspectionRecordCategory\\n        inspectionId\\n        exemption\\n      }\\n      inspectionRecords {\\n        id\\n        inspectionRecordId\\n        exemption\\n      }\\n      defectRefs {\\n        id\\n        defectId\\n        defectQty\\n      }\\n      subDefects {\\n        id\\n        type\\n        defectId\\n        defectQty\\n        exemption\\n      }\\n      seller {\\n        sellerId\\n      }\\n      resolutions {\\n        id\\n        type\\n        state\\n        acceptedQty\\n        remainingQty\\n        createdOn\\n      }\\n      actionImplementations {\\n        shipPoint\\n        implementationDate\\n        marketType\\n        implementationDc\\n      }\\n      inspectionInfo {\\n        id\\n        exemption\\n      }\\n      totalComment\\n    }\\n    pageInfo {\\n      totalCount\\n      totalPages\\n      pageNumber\\n      pageSize\\n      filterCount\\n      hasNext\\n      hasPrev\\n      pageContinuationToken\\n    }\\n  }\\n}\",\"variables\":{\"searchInput\":{\"dcNumber\":6020,\"exceptionCategory\":\"PROBLEM\",\"status\":[\"AWAITING_INFORMATION\",\"ANSWERED\",\"REASSIGNED\",\"ASSIGNED\"],\"delivery\":{\"number\":\""
					+ x
					+ "\"},\"createdOn\":[\"2021-01-01\",\"2022-05-01\"]},\"pageSize\":50,\"sortDirection\":\"ASC\"}}";
			StringEntity entity = new StringEntity(json);
			httpPost1.setEntity(entity);
			httpPost1.setHeader("Accept", "application/json");
			httpPost1.setHeader("Content-type", "application/json");
			httpPost1.setHeader("WM_SVC.NAME", "FIXIT-PLATFORM");
			httpPost1.setHeader("WM_CONSUMER.ID", "bad65007-00e8-4924-91ef-4dfac4482d47");
			httpPost1.setHeader("WM_SVC.ENV", "FIXIT-PROD");
			httpPost1.setHeader("WMT-Channel", "WEB");
			httpPost1.setHeader("WMT-Source", "FIXIT");
			httpPost1.setHeader("facilityNum", "6020");
			httpPost1.setHeader("facilityCountryCode", "US");
			httpPost1.setHeader("Origin", "altair://-");
			httpPost1.setHeader("Connection", "keep-alive");
			httpPost1.setHeader("Accept-Encoding", "gzip, deflate, br");
			HttpResponse response = client.execute(httpPost1);

			System.out.println(response.getStatusLine().getStatusCode());
			String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			JSONObject jsonObject = new JSONObject(responseBody);

			// String data="{'name':{'age':{'color':[{'pink':'ppink','red':'rred'}]}}}";
			System.out.println("Response body: " + jsonObject);
			JSONArray pilot = jsonObject.getJSONObject("data").getJSONObject("searchException").getJSONArray("issues");
			JSONObject rec = pilot.getJSONObject(0);

			String ex_id = rec.getString("exceptionId");

			System.out.println("Exception Id for current Iteration= " + ex_id);

			listOfExceptionIds.add(ex_id);
		}

		// STEP-3
		//////////////////////////////////////////////////////////////////////////////
		////////////////// TO CANCEL THE TICKET WITH GIVEN EXCEPTION ID////////////
		//////////////////////////////////////////////////////////////////////////////

		for (String ex_id : listOfExceptionIds) {
			HttpPost httpPost2 = new HttpPost("https://api.fixit.platform.prod.us.walmart.net/graphql");

			String json2 = "{\"query\":\"mutation {\\n  closeException(\\n    exceptionId: \\\" " + ex_id
					+ "  \\\"\\n    userInfo: { userId: \\\"user\\\", userName: \\\"p0t02g2\\\" }\\n  ) {\\n    exceptionId\\n    identifier\\n  }\\n}\\n\\t\",\"variables\":{}}";
			StringEntity entity2 = new StringEntity(json2);
			httpPost2.setEntity(entity2);
			httpPost2.setHeader("Connection", "keep-alive");
			httpPost2.setHeader("Accept-Encoding", "gzip, deflate, br");
			httpPost2.setHeader("Accept", "application/json");
			httpPost2.setHeader("Content-type", "application/json");
			httpPost2.setHeader("WMT-Channel", "NOP");
			httpPost2.setHeader("WMT-Source", "FIX_IT");
			httpPost2.setHeader("facilityNum", "6020");
			httpPost2.setHeader("facilityCountryCode", "US");
			httpPost2.setHeader("Origin", "altair://-");

			HttpResponse response2 = client.execute(httpPost2);

			String responseBody2 = EntityUtils.toString(response2.getEntity(), StandardCharsets.UTF_8);
			System.out.println(responseBody2);
		}

	}
}

//public class ExeecuteShellComandProcessBuilder {
//	public static void main(String[] args) throws IOException, InterruptedException, AuthenticationException,
//			KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
//
//		List<String> listOfDeliveryNumbers = new ArrayList<String>();
//		List<String> listOfExceptionIds = new ArrayList<String>();
//
//		// STEP-1
//		//////////////////////////////////////////////////////////////////////////////
//		////////////// GETTING ALL THE DELIVERY NUMBERS FROM THE EXCEL////////////
//		//////////////////////////////////////////////////////////////////////////////
//		FileInputStream file = new FileInputStream(new File("/Users/p0t02g2/Desktop/DNumber.xlsx"));//add your file path
//		XSSFWorkbook workbook = new XSSFWorkbook(file);
//		XSSFSheet sheet = workbook.getSheetAt(0);
//		DataFormatter formatter = new DataFormatter();
//		for (int i = 1; i < 1606; i++) {
//			String k1 = formatter.formatCellValue(sheet.getRow(i).getCell(0));// number
//			listOfDeliveryNumbers.add(k1);
//		}
//
//		// STEP-2
//		//////////////////////////////////////////////////////////////////////////////
//		////////////// FIND EXCEPTION ID FROM DELIVERY NUMBER///////////////////////
//		//////////////////////////////////////////////////////////////////////////////
//		HttpClient client = HttpClients.custom()
//				.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
//				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
//		HttpPost httpPost1 = new HttpPost("https://api.fixit.platform.prod.us.walmart.net/graphql");
//
//		for (String x : listOfDeliveryNumbers) {
//			String json = "{\"query\":\"query SearchInspection(\\n  $searchInput: ExceptionSearchInput!\\n  $pageSize: Int!\\n  $pageContinuationToken: String\\n  $sortBy: String\\n  $sortDirection: SortDirection\\n) {\\n  searchException(\\n    searchInput: $searchInput\\n    pageSize: $pageSize\\n    pageContinuationToken: $pageContinuationToken\\n    sortBy: $sortBy\\n    sortDirection: $sortDirection\\n  ) {\\n    issues {\\n      exceptionId\\n      identifier\\n      dcNumber\\n      countryCode\\n      sellerType\\n      exceptionCategory\\n      exceptionSubCategory\\n      status\\n      statusReason\\n      createdBy\\n      createdOn\\n      modifiedBy\\n      modifiedOn\\n      assignment {\\n        assignedUser\\n      }\\n      delivery {\\n        id\\n        number\\n        scac\\n        trailerNumber\\n        load\\n        additionalInfo\\n        frieghtOrigin\\n        frieghtOriginCenter\\n      }\\n      cases{\\n        id\\n        label\\n        qty\\n        remainingQty\\n        slot\\n      }\\n      items {\\n        id\\n        itemNumber\\n        itemDescription\\n        departmentNumber\\n        itemUpc\\n        vendorName\\n        vendorNumber\\n        vendorStockNumber\\n        vnpkCost\\n        whpkCost\\n        vnpkQty\\n        whpkQty\\n        vnpk\\n        whpk\\n        receivedVnpkQty\\n        receivedWhpkQty\\n        qtyUom\\n        hazmat\\n        sellByDate\\n      }\\n     purchaseOrders {\\n        id\\n        event\\n        poType\\n        poNumber\\n        casePoNumber\\n        bookingPONumber\\n        poStatus\\n        poChannel\\n        poLineNumber\\n        poLineStatus\\n        orderedQty\\n        qtyUom\\n        receivedQtyTillDate\\n        shipDate\\n        cancelDate\\n        mabd\\n        partnerId\\n        additionalInfo\\n      }\\n      vendor {\\n        id\\n        vendorName\\n        vendorNumber\\n        vendorDeptNumber\\n      }\\n      details {\\n        id\\n        inspectionQty\\n        exceptionQty\\n        remainingQty\\n        exceptionUOM\\n        referenceTicket\\n        exceptionType\\n        businessStatus\\n        exemption\\n      }\\n      seller {\\n        id\\n        sellerName\\n      }\\n      defects {\\n        defectId\\n        type\\n        subType\\n        defectQty\\n        updatedOn\\n      }\\n      defectDetail {\\n        id\\n        inspectionQty\\n        noDefectQty\\n        defectQty\\n        type\\n        inspectionId\\n        inspectionRecordId\\n        inspectionRecordDisplayId\\n        inspectionRecordCategory\\n        qtyUom\\n        exemption\\n      }\\n      inspectionRecordDetail {\\n        id\\n        inspectionQty\\n        noDefectQty\\n        inspectionRecordCategory\\n        inspectionId\\n        exemption\\n      }\\n      inspectionRecords {\\n        id\\n        inspectionRecordId\\n        exemption\\n      }\\n      defectRefs {\\n        id\\n        defectId\\n        defectQty\\n      }\\n      subDefects {\\n        id\\n        type\\n        defectId\\n        defectQty\\n        exemption\\n      }\\n      seller {\\n        sellerId\\n      }\\n      resolutions {\\n        id\\n        type\\n        state\\n        acceptedQty\\n        remainingQty\\n        createdOn\\n      }\\n      actionImplementations {\\n        shipPoint\\n        implementationDate\\n        marketType\\n        implementationDc\\n      }\\n      inspectionInfo {\\n        id\\n        exemption\\n      }\\n      totalComment\\n    }\\n    pageInfo {\\n      totalCount\\n      totalPages\\n      pageNumber\\n      pageSize\\n      filterCount\\n      hasNext\\n      hasPrev\\n      pageContinuationToken\\n    }\\n  }\\n}\",\"variables\":{\"searchInput\":{\"dcNumber\":6020,\"exceptionCategory\":\"PROBLEM\",\"status\":[\"AWAITING_INFORMATION\",\"ANSWERED\",\"REASSIGNED\",\"ASSIGNED\"],\"delivery\":{\"number\":\""
//					+ x
//					+ "\"},\"createdOn\":[\"2021-01-01\",\"2022-05-01\"]},\"pageSize\":50,\"sortDirection\":\"ASC\"}}";
//			StringEntity entity = new StringEntity(json);
//			httpPost1.setEntity(entity);
//			httpPost1.setHeader("Accept", "application/json");
//			httpPost1.setHeader("Content-type", "application/json");
//			httpPost1.setHeader("WM_SVC.NAME", "FIXIT-PLATFORM");
//			httpPost1.setHeader("WM_CONSUMER.ID", "bad65007-00e8-4924-91ef-4dfac4482d47");
//			httpPost1.setHeader("WM_SVC.ENV", "FIXIT-PROD");
//			httpPost1.setHeader("WMT-Channel", "WEB");
//			httpPost1.setHeader("WMT-Source", "FIXIT");
//			httpPost1.setHeader("facilityNum", "6020");
//			httpPost1.setHeader("facilityCountryCode", "US");
//			httpPost1.setHeader("Origin", "altair://-");
//			httpPost1.setHeader("Connection", "keep-alive");
//			httpPost1.setHeader("Accept-Encoding", "gzip, deflate, br");
//			HttpResponse response = client.execute(httpPost1);
//
//			System.out.println(response.getStatusLine().getStatusCode());
//			String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//			JSONObject jsonObject = new JSONObject(responseBody);
//
//			// String data="{'name':{'age':{'color':[{'pink':'ppink','red':'rred'}]}}}";
//			System.out.println("Response body: " + jsonObject);
//			JSONArray pilot = jsonObject.getJSONObject("data").getJSONObject("searchException").getJSONArray("issues");
//			JSONObject rec = pilot.getJSONObject(0);
//
//			String ex_id = rec.getString("exceptionId");
//
//			System.out.println("Exception Id for current Iteration= " + ex_id);
//
//			listOfExceptionIds.add(ex_id);
//		}
//
//		// STEP-3
//		//////////////////////////////////////////////////////////////////////////////
//		////////////////// TO CANCEL THE TICKET WITH GIVEN EXCEPTION ID////////////
//		//////////////////////////////////////////////////////////////////////////////
//
//		for (String ex_id : listOfExceptionIds) {
//			HttpPost httpPost2 = new HttpPost("https://api.fixit.platform.prod.us.walmart.net/graphql");
//
//			String json2 = "{\"query\":\"mutation {\\n  closeException(\\n    exceptionId: \\\" " + ex_id
//					+ "  \\\"\\n    userInfo: { userId: \\\"user\\\", userName: \\\"p0t02g2\\\" }\\n  ) {\\n    exceptionId\\n    identifier\\n  }\\n}\\n\\t\",\"variables\":{}}";
//			StringEntity entity2 = new StringEntity(json2);
//			httpPost2.setEntity(entity2);
//			httpPost2.setHeader("Connection", "keep-alive");
//			httpPost2.setHeader("Accept-Encoding", "gzip, deflate, br");
//			httpPost2.setHeader("Accept", "application/json");
//			httpPost2.setHeader("Content-type", "application/json");
//			httpPost2.setHeader("WMT-Channel", "NOP");
//			httpPost2.setHeader("WMT-Source", "FIX_IT");
//			httpPost2.setHeader("facilityNum", "6020");
//			httpPost2.setHeader("facilityCountryCode", "US");
//			httpPost2.setHeader("Origin", "altair://-");
//
//			HttpResponse response2 = client.execute(httpPost2);
//
//			String responseBody2 = EntityUtils.toString(response2.getEntity(), StandardCharsets.UTF_8);
//			System.out.println(responseBody2);
//		}
//
//	}
//}
