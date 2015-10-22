package com.hewei.spider.csdn.processor;

import com.google.common.collect.Lists;
import com.hewei.spider.csdn.constants.CsdnConstants;
import com.hewei.spider.csdn.pojos.CsdnActivity;
import com.hewei.spider.csdn.pojos.CsdnActivityDetails;
import com.hewei.spider.csdn.site.CsdnSite;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:14
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CsdnProcessor.class);

    private Set<String> urlSet = new HashSet<>();

    private Set<String> activitySet = new HashSet<>();

    @Override
    public void process(Page page) {

        String url = page.getUrl().get();

        logger.info("process url:{}", url);

        if (!url.startsWith(CsdnConstants.ORIGINAL_URL.substring(0, CsdnConstants.ORIGINAL_URL.lastIndexOf("/home")))) {
            logger.info("external csdn url:{}", url);
            return;
        }

        if (CsdnConstants.ORIGINAL_URL.equals(url)) {
            pagesDeal(page);
            activityDeal(page);
            return;
        }

        if (urlSet.contains(url)) {
            pagesDeal(page);
            activityDeal(page);
            return;
        }

        if (activitySet.contains(url)) {
            activityDetails(page);
            return;
        }

        logger.info("no deal url:{}", url);
    }

    private void activityDetails(Page page) {

        Selectable main = page.getHtml().xpath("//div[@class='main clearfix']");

        if(StringUtils.isEmpty(main.get())){
            return;
        }

        Selectable head = main.xpath("//div[@class='act-head']/dl");

        if(StringUtils.isEmpty(head.get())){
            return;
        }

        Html headHtml = Html.create(head.get().replace("dt>", "dd>"));

        String title = headHtml.xpath("//dd[1]/h2/text()").get();

        String dizhi = headHtml.xpath("//dd[2]/text()").get().trim();

        String[] addrTime = dizhi.split("  ");

        if(addrTime.length != 2){
            addrTime=dizhi.split("   ");
        }

        String addr = addrTime.length == 2 ? addrTime[0] : dizhi;
        String time = addrTime.length == 2 ? addrTime[1] : "";

        Selectable tbody = main.xpath("//div[@class='main clearfix']/table/tbody");

        String signType = tbody.xpath("//tr/td[1]/text()").get();


        String signStopTime = tbody.xpath("//tr/td[2]/text()").get();

        String content = main.xpath("//div[@class='intro dec']").get();


//        Selectable addrTime= main.xpath("//div[@class='addr-time']");
//
//        String time = addrTime.xpath("//ul/li[1]/text()").get();
//        System.out.println("time:" + time);
//        String addr = addrTime.xpath("//ul/li[2]/text()").get();
//        System.out.println("addr:" + addr);
        page.putField(CsdnConstants.ACTIVITY_DETAILS, new CsdnActivityDetails(title, addr, time, signType, signStopTime, content));

    }

    private void pagesDeal(Page page) {

        List<String> pages = page.getHtml().xpath("//div[@class='csdn-pagination hide-set hide-go']//a/text()").all();

        Set<String> pageUrlSet = new HashSet<>(pages.size());

        for (String message : pages) {
            if (!isNumeric(message)) {
                continue;
            }

            String newUrl = CsdnConstants.ORIGINAL_URL + "?&page=" + message;
            logger.info("newUrl:{}", newUrl);
            pageUrlSet.add(newUrl);
        }

        urlSet.addAll(pageUrlSet);

        newDeal(page, pageUrlSet);

    }

    private void activityDeal(Page page) {

        List<String> activities = page.getHtml().xpath("//div[@class='item clearfix']").all();

        List<CsdnActivity> activityList = new ArrayList<>(activities.size());

        Set<String> actSet = new HashSet<>(activities.size());

        for (String message : activities) {

            Html html = Html.create(message);

            Selectable wraper = html.xpath("//div[@class='time-wraper']");

            String dayTime = wraper.xpath("//div[@class='month']/text()").get();

            String addr = wraper.xpath("//div[@class='addr']/text()").get();

            Selectable dis = html.xpath("//div[@class='dis']/dl");

            String url  = dis.xpath("//dt").links().get();

            String title  = dis.xpath("//dt/a/text()").get();

            String expense  = dis.xpath("//dd[1]/text()").get();

            String specificTime  = dis.xpath("//dd[2]/text()").get();

            String location  = dis.xpath("//dd[3]/a/text()").get();

            activityList.add(new CsdnActivity(dayTime, addr, url, title, expense, specificTime, location));

            actSet.add(url);
        }

        page.putField(CsdnConstants.ACTIVITY, activityList);

        activitySet.addAll(actSet);

        newDeal(page, actSet);

    }

    private void newDeal(Page page, Set<String> set) {
        page.addTargetRequests(Lists.newArrayList(set));
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s="深圳会展中心（深圳市福田中心区福华三路深圳会展中心）   2016年04月08日 - 2016年04月10日";
        System.out.println(s.split("    ").length);
    }

    @Override
    public Site getSite() {
        return CsdnSite.getSite();
    }
}
