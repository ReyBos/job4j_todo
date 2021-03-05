package ru.reybos.servlet;

import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.store.HbmStore;
import ru.reybos.store.MemStore;
import ru.reybos.store.Store;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.hibernate.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(HbmStore.class)
public class ItemServletTest {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServletTest.class.getName());

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void saveItem() throws Exception {
        Store store = MemStore.instOf();
        File source = folder.newFile("source.txt");
        Item inputItem = new Item("Какое-то дело");
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        BufferedReader in = new BufferedReader(new FileReader(source));
        ServletOutputStream out = mock(ServletOutputStream.class);
        PowerMockito.mockStatic(HbmStore.class);

        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("save");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);

        new ItemServlet().doPost(req, resp);
        assertThat(store.findAllItem().get(0).getDescription(), is("Какое-то дело"));
    }

    @Test
    public void updateItem() throws Exception {
        Store store = MemStore.instOf();
        File source = folder.newFile("source.txt");
        Item item = new Item("Какое-то дело", false);
        store.save(item);
        Item inputItem = new Item(item.getId(), true);
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        BufferedReader in = new BufferedReader(new FileReader(source));
        ServletOutputStream out = mock(ServletOutputStream.class);
        PowerMockito.mockStatic(HbmStore.class);

        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("update");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);

        new ItemServlet().doPost(req, resp);
        LOG.debug(store.findAllItem().toString());
        assertThat(store.findItemById(inputItem.getId()).isDone(), is(true));
    }

    @Test
    public void deleteItem() throws Exception {
        Store store = MemStore.instOf();
        File source = folder.newFile("source.txt");
        Item item = new Item("Какое-то дело", false);
        store.save(item);
        Item inputItem = new Item(item.getId());
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        BufferedReader in = new BufferedReader(new FileReader(source));
        ServletOutputStream out = mock(ServletOutputStream.class);
        PowerMockito.mockStatic(HbmStore.class);

        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("delete");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);

        new ItemServlet().doPost(req, resp);
        assertNull(store.findItemById(inputItem.getId()));
    }
}