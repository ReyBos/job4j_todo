package ru.reybos.servlet;

import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
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
import ru.reybos.model.User;
import ru.reybos.store.HbmStore;
import ru.reybos.store.MemStore;
import ru.reybos.store.Store;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

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
    private Store store;
    private File source;
    private User user;
    private Item item;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private BufferedReader in;
    private ServletOutputStream out;

    @Before
    public void init() throws IOException {
        store = MemStore.instOf();
        source = folder.newFile("source.txt");
        user = User.of("user", "email", "password");
        store.save(user);
        item = Item.of("Какое-то дело", false, user);
        store.save(item);

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        in = new BufferedReader(new FileReader(source));
        out = mock(ServletOutputStream.class);
    }

    @After
    public void clear() {
        store.delete(user);
        store.delete(item);
    }

    /**
     * user и item хранятся в базе данных. inputItem нужен для имитации пришедших данных с формы,
     * предварительно сохраняем json представление этого объекта в файле, что бы потом можно было
     * прочитать его
     */
    @Test
    public void saveItem() throws Exception {
        Item inputItem = new Item();
        inputItem.setDescription("Какое-то дело");
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        PowerMockito.mockStatic(HbmStore.class);
        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("save");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);
        when(req.getSession()).thenReturn(session);
        when((session.getAttribute("user"))).thenReturn(user);

        new ItemServlet().doPost(req, resp);
        Item itemDb = store.findAllItem().get(0);
        assertThat(itemDb.getDescription(), is("Какое-то дело"));
        assertThat(itemDb.getUser(), is(user));
    }

    @Test
    public void updateItem() throws Exception {
        Item inputItem = new Item();
        inputItem.setId(item.getId());
        inputItem.setDone(true);
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        PowerMockito.mockStatic(HbmStore.class);
        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("update");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);
        when(req.getSession()).thenReturn(session);
        when((session.getAttribute("user"))).thenReturn(user);

        new ItemServlet().doPost(req, resp);
        Item itemDb = store.findItemById(inputItem.getId());
        assertThat(itemDb.isDone(), is(true));
        assertThat(itemDb.getUser(), is(user));
    }

    @Test
    public void deleteItem() throws Exception {
        Item inputItem = new Item();
        inputItem.setId(item.getId());
        String itemJson = new GsonBuilder().create().toJson(inputItem);
        try (PrintWriter out = new PrintWriter(source)) {
            out.write(itemJson);
        }

        PowerMockito.mockStatic(HbmStore.class);
        Mockito.when(HbmStore.instOf()).thenReturn(store);
        when(req.getParameter("action")).thenReturn("delete");
        when(req.getReader()).thenReturn(in);
        when(resp.getOutputStream()).thenReturn(out);
        when(req.getSession()).thenReturn(session);
        when((session.getAttribute("user"))).thenReturn(user);

        new ItemServlet().doPost(req, resp);
        assertNull(store.findItemById(inputItem.getId()));
    }
}