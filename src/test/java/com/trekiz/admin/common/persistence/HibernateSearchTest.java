//package com.trekiz.admin.common.persistence;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JsonConfig;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.trekiz.admin.common.test.SpringTransactionalContextTests;
//import com.trekiz.admin.modules.lucene.entity.Activity;
//import com.trekiz.admin.modules.lucene.service.IndexService;
//
//
///**
// * HibernateSearchTest
// * @author Administrator
// *
// */
//public class HibernateSearchTest extends SpringTransactionalContextTests {
//
//	@Autowired
//	private IndexService indexService;
////	@Autowired
////	private AreaUtil areaUtil;
//	
////	@Test
////	public void testSearchDB1() throws IOException {
////		try {
////			List<Activity> list = indexService.findAll();
////			System.out.println("list size:" + list.size());
////			for (Activity ac : list) {
////				for(Price4Activity p : ac.getPrice4Activitys()){
////					System.out.println(p.getPriceDate());
////				}
////				
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		
////
////	}
//	
//	@Test
//	public void testIKIndex(){
//		List<Activity> dataList = new ArrayList<Activity>();
////		List<ActivityTest> dataList = new ArrayList();
////		List<Price4Activity> dataList1 = new ArrayList<Price4Activity>();
//		try {
//			
////			areaUtil.serverInit();
//			//多个实体逐一建立索引
////			indexService.initActivityIndex();
////			indexService.initMainActivityIndex();
////			indexService.initActivityServiceLevelIndex();
////			indexService.initCityIndex();
////			indexService.initPrice4ActivityIndex();
////			indexService.initProvinceIndex();
//			
//			//多线程并发重建索引
//			indexService.rebuildAllIndex(Activity.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(ActivityTest.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(MainActivity.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(ActivityServiceLevel.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(Picture.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(City.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(Price4Activity.class, 10000, 5, 10, 50);
////			indexService.rebuildAllIndex(Province.class, 10000, 5, 10, 50);
//			
////			indexService.rebuildAllIndex(MainActivityCopy.class, 10000, 5, 10, 50);
//			
//			dataList = indexService.searchActivityByName("mainActivity.city.province.provinceNameCn", "北京");
////			dataList = indexService.luceneSearchByName("name", "");
//			JsonConfig jsonConfig = new JsonConfig();
//			//过滤掉反向循环的属性
//			jsonConfig.setExcludes(new String[]{"activity","mainActivitys","citys"});
//			
//			for(Activity p:dataList){
////				System.out.println(p.getActivity_name());
//				String s = JSONArray.fromObject(p,jsonConfig).toString();
//				System.out.println(s);
////				System.out.println(p.getActivity_name()+"|"+p.getMainActivity().getCity().getCityName()+"|"+p.getMainActivity().getCity().getProvince().getCitys().size());
////				System.out.println(p.getActivity_name()+"||"+p.getMainActivity().getCity().getCityName());
////				Set<Price4Activity> sets = p.getPrice4Activitys();
////				Iterator<Price4Activity> it = sets.iterator();
////				while(it.hasNext()){
////					Price4Activity price = it.next();
////					System.out.println(p.getActivity_name()+"||"+price.getPriceDate());
////				}
//					
//				
//			}
////			for(Price4Activity price:dataList1){
////				System.out.println(price.getPriceDate());
////			}
//			
////			ActivityTest activity = new ActivityTest();
////			MainActivityTest mainActivity = new MainActivityTest();
////			mainActivity.setName("ljm");
////			activity.setName("梁景明");
////			activity.setShortInfo("地狱男爵");
////			activity.setMainActivity(mainActivity);
////			
////			indexService.saveObj(activity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
////	@Test
////	public void joinSearch(){
////		try {
////			Analyzer analyzer = new IKAnalyzer();
////			Directory directory = new RAMDirectory();
////			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,analyzer);
////			IndexWriter w = new IndexWriter(directory, iwc);
////			
////			Document doc = new Document();
////			doc.add(new Field("description", "random text1",Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("name", "name1", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("id", "1", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			w.addDocument(doc);
////			
////			doc = new Document();
////			doc.add(new Field("description", "random text2",Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("name", "name2", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("id", "2", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			w.addDocument(doc);
////			
////			doc = new Document();
////			doc.add(new Field("price", "10",Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("id", "3", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("productId", "1", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			w.addDocument(doc);
////			
////			doc = new Document();
////			doc.add(new Field("price", "20",Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("id", "4", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			doc.add(new Field("productId", "2", Field.Store.YES,Field.Index.NOT_ANALYZED));
////			w.addDocument(doc);
////			w.close();
////			
////			IndexReader reader = IndexReader.open(directory);
////			//创建一个搜索 
////			IndexSearcher indexSearcher = new IndexSearcher(reader);
////			
//////			IndexSearcher indexSearcher = new IndexSearcher(w.getReader());   
////			Query joinQuery =  
////			JoinUtil.createJoinQuery("id", "productId", new TermQuery(new Term("name", "name2")), indexSearcher);  
////			ScoreDoc[] hits = indexSearcher.search(joinQuery, 10).scoreDocs;
////	        for (ScoreDoc match : hits) {
////				Document doc1 = indexSearcher.doc(match.doc);
////				System.out.println("" + doc1.get("description"));
////			}
////		} catch (Exception e) {
////			// TODO: handle exception
////		}
////	}
//}
