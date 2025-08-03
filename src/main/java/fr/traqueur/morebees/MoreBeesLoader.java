package fr.traqueur.morebees;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * MoreBeesLoader is a class that implements the PluginLoader interface.
 * It is responsible for loading the necessary libraries for the MoreBees plugin.
 */
public class MoreBeesLoader implements PluginLoader {
    @Override
    public void classloader(PluginClasspathBuilder pluginClasspathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        /*resolver.addRepository(new RemoteRepository.Builder(
                "central", "default", "https://maven-central.storage-download.googleapis.com/maven2"
        ).build());*/ // Uncomment if you want to use Maven Central
        resolver.addRepository(new RemoteRepository.Builder(
                "jitpack", "default", "https://jitpack.io"
        ).build());


        InputStream stream = getClass().getClassLoader().getResourceAsStream("plugin.yml");
        if (stream == null) {
            throw new IllegalStateException("plugin.yml not found in classpath!");
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

        List<String> libs = config.getStringList("libraries");

        for (String lib : libs) {
            resolver.addDependency(new Dependency(
                    new DefaultArtifact(lib), null
            ));
        }
        pluginClasspathBuilder.addLibrary(resolver);
    }
}
