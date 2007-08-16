package nl.gridshore.paymentsample.httpunit;

import com.meterware.httpunit.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jettro.Coenradie
 * Date: 16-aug-2007
 * Time: 23:09:02
 * To change this template use File | Settings | File Templates.
 */
public class UpdateStatusOfOpenOrders {

    public static List<String> doUpdate() {
        List<String> results = new ArrayList<String>();
        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest("http://localhost:8080/payments/viewPayments.html");
        WebResponse response = null;
        WebLink[] links = new WebLink[0];
        try {
            response = conversation.getResponse(request);
            links = response.getLinks();
        } catch (IOException e) {
            results.add("IO Problem while obtaining the payments (and links)");
        } catch (SAXException e) {
            results.add("IO Problem while obtaining the payments (and links)");
        }
        if (links == null || links.length == 0) {
            results.add("No open payments found to be updated");
        } else {
            for (WebLink link : links) {
                String requestedId = link.getParameterValues("id")[0];
                try {
                    handleLink(conversation, link);
                } catch (IOException e) {
                    results.add("IO Problem while handling payment with id " + requestedId);
                } catch (SAXException e) {
                    results.add("IO Problem while handling payment with id " + requestedId);
                }
                results.add("update payment with id : " + requestedId);
            }
        }
        return results;
    }

    private static void handleLink(WebConversation conversation, WebLink link) throws IOException, SAXException {
        link.click();
        WebResponse formPage = conversation.getCurrentPage();
        WebForm form = formPage.getForms()[0];
        form.setParameter("status","paid");
        form.submit();
    }

    public static void main(String[] args) throws IOException, SAXException {
        List<String> results = doUpdate();
        for (String result : results) {
            System.out.println(result);
        }
    }
}