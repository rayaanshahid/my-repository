import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.rayaan.blogpost.AdditionalConfig;
import com.rayaan.blogpost.kafka.event.KafkaBlogpostEventProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.junit.*;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.Collection;

public class BlogRepositoryIT {
    @ClassRule
    public static DockerComposeContainer testdb =
            new DockerComposeContainer(new File("docker-compose.yml"))
                    .withExposedService("db_1", 5432,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withExposedService("kafka_1", 9092,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    private IDatabaseConnection con;

    @Before
    public void initPostgresConnection() throws Exception{

        String host = testdb.getServiceHost("db_1",5432);
        int port = testdb.getServicePort("db_1",5432);
        String db = "myDB";
        String user = "dbuser";
        String pass = "myblogs";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

        Connection c = DriverManager.getConnection(url, user, pass);
        c.setAutoCommit(false);
        DatabaseConnection con = new DatabaseConnection(c);
        //con.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        con.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY , new PostgresqlDataTypeFactory());

        this.con =con;
    }

    @After
    public void rollback() throws Exception{
        this.con.getConnection().rollback();
        this.con.getConnection().close();
    }

    @Test
    public void testProducerConsumer() {
        String host = testdb.getServiceHost("kafka_1",9092);
        Integer port = testdb.getServicePort("kafka_1", 9092);

        AdditionalConfig additionalConfig = new AdditionalConfig();
        KafkaBlogpostEventProducer kafkaBlogpostEventProducer = new KafkaBlogpostEventProducer(host+":"+port,"test_topic");
        kafkaBlogpostEventProducer.produce("Test","Blog Tested");

        HelloConsumer helloConsumer = new HelloConsumer(host+":"+port);
        helloConsumer.consume();
        Collection<ConsumerRecord> messages = helloConsumer.getReceivedRecords();

        Assert.assertEquals("message consumed", messages.size(), 1);
        messages.forEach(stringStringConsumerRecord -> {
            Assert.assertEquals(stringStringConsumerRecord.key(), "Test");
            Assert.assertEquals(stringStringConsumerRecord.value(), "Blog Tested");
        });
    }

    @Test
    public void testSelect() throws Exception{

        IDataSet res =  con.createDataSet();

        ITable product = res.getTable("PRODUCT");
        assertThat(product.getRowCount(), is(1));

        ITable div = res.getTable("DIV");

        assertThat(div.getRowCount(), is(4));
    }


    @Test
    public void testInsert() throws Exception{

        con.getConnection().prepareStatement("INSERT INTO PRODUCT VALUES(8, 'productName', 3, 1)").executeUpdate();

        IDataSet res =  con.createDataSet();
        ITable product = res.getTable("PRODUCT");
        assertThat(product.getRowCount(), is(2));
        assertThat(product.getValue(1, "name"), is("productName"));
    }

}
