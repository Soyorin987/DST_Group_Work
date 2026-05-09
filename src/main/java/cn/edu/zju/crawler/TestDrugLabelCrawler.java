package cn.edu.zju.crawler;

public class TestDrugLabelCrawler {
    public static void main(String[] args) {
        DrugLabelCrawler crawler = new DrugLabelCrawler();
        crawler.doCrawlerDrugLabel();
        System.out.println("Drug label crawler test finished.");
    }
}