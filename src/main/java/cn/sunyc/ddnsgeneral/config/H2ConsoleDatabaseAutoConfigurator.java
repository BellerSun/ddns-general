package cn.sunyc.ddnsgeneral.config;

import org.h2.engine.Constants;
import org.h2.store.fs.FileUtils;
import org.h2.util.SortedProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * H2数据库初始化,否则H2数据库不允许自动创建库。<br>
 * 详情见 <a href="https://stackoverflow.com/questions/61865206/springboot-2-3-0-while-connecting-to-h2-database">stackoverflow</a>
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class H2ConsoleDatabaseAutoConfigurator {

    @Autowired
    public void configure(ObjectProvider<DataSource> dataSource) throws Exception {
        final Properties properties = SortedProperties.loadProperties(Constants.SERVER_PROPERTIES_DIR + "/" + Constants.SERVER_PROPERTIES_NAME);

        final List<String> urls = dataSource.orderedStream().map((available) -> {
            try (Connection connection = available.getConnection()) {
                if (connection.getMetaData().getURL().startsWith("jdbc:h2:file:")) {
                    return connection.getMetaData().getURL() + "|" + connection.getMetaData().getUserName();
                } else {
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (urls.size() > 0) {
            for (int i = 0; ; i++) {
                final String value = properties.getProperty(String.valueOf(i), null);
                if (value == null || value.startsWith("Local H2|")) {
                    properties.setProperty(String.valueOf(i), "Local H2|org.h2.Driver|" + urls.get(0));
                    break;
                }
            }

            final OutputStream out = FileUtils.newOutputStream(Constants.SERVER_PROPERTIES_DIR + "/" + Constants.SERVER_PROPERTIES_NAME, false);
            properties.store(out, "H2 Server Properties");
            out.close();
        }
    }
}
