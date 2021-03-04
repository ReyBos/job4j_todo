package ru.reybos.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.service.TodoListService;
import ru.reybos.store.HbmStore;
import ru.reybos.store.MemStore;
import ru.reybos.store.Store;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.hibernate.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({HbmStore.class, TodoListServlet.class})
public class TodoListServletTest {
    private static final Logger LOG = LoggerFactory.getLogger(TodoListServletTest.class.getName());

    @Test
    public void getItems() throws Exception {
        Store store = MemStore.instOf();
        Item item1 = new Item("задание 1", true);
        Item item2 = new Item("задание 2", false);
        Item item3 = new Item("задание 3", true);
        store.save(item1);
        store.save(item2);
        store.save(item3);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        ServletOutputStream out = mock(ServletOutputStream.class);
        PowerMockito.mockStatic(HbmStore.class);
        PowerMockito.mockStatic(PrintWriter.class);

        Mockito.when(HbmStore.instOf()).thenReturn(store);
        PowerMockito.whenNew(PrintWriter.class).withAnyArguments().thenReturn(writer);
        when(resp.getOutputStream()).thenReturn(out);

        new TodoListServlet().doPost(req, resp);

        TodoListService service = new TodoListService(store);
        assertThat(stringWriter.toString(), is(service.getAllItems()));
        store.delete(item1);
        store.delete(item2);
        store.delete(item3);
    }
}