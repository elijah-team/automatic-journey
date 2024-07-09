package rifers;

import rife.bld.WebProject;
import rife.bld.dependencies.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.TemplateType.*;

public class RifersBuild extends WebProject {
	public RifersBuild() {
		pkg              = "rifers";
		name             = "Rifers";
		mainClass        = "rifers.RifersSite";
		uberJarMainClass = "rifers.RifersSiteUber";
		version          = version(1, 0, 0);

		downloadSources   = true;
		autoDownloadPurge = true;

		final String V_activej_version   = "6.0-SNAPSHOT";
		final String V_jdeferred_version = "2.0.0";
		final String V_guava_version   = "33.1.0-jre";
		final String V_buffers_version = "0.0.3";
		final String V_range_version   = "0.0.3b";

		final Repository GITLAB_MAVEN_BUFFERS = new Repository("https://gitlab.com/api/v4/projects/20346374/packages/maven");
		final Repository GITLAB_MAVEN_RANGE = new Repository("https://gitlab.com/api/v4/projects/21223510/packages/maven");
		final Repository JITPACK = new Repository("https://jitpack,io");

		repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES, GITLAB_MAVEN_RANGE, GITLAB_MAVEN_BUFFERS, JITPACK);

		scope(compile)
				.include(dependency("com.uwyn.rife2", "bld", "1.9.1")) // ??
				.include(dependency("com.uwyn.rife2", "rife2", version(1, 7, 3)))
				.include(dependency("com.uwyn.rife2", "rife2-core", "1.8.1"))
				.include(dependency("com.uwyn.rife2", "rife2-renderers", "1.1.5"))
				.include(dependency("com.fasterxml.jackson.datatype", "jackson-datatype-pcollections", "2.17.2"))
				.include(dependency("org.pcollections", "pcollections", "4.0.2"))
				.include(dependency("io.activej", "activej-net", "6.0-beta2"))
				.include(dependency("io.activej", "activej-inject", V_activej_version))
				.include(dependency("io.activej", "activej-launcher", V_activej_version))
				.include(dependency("io.activej", "activej-csp", V_activej_version))
				.include(dependency("io.activej", "activej-common", V_activej_version))
				.include(dependency("io.activej", "activej-http", V_activej_version))
				.include(dependency("io.activej", "activej-eventloop", V_activej_version))
				.include(dependency("io.activej", "activej-launchers-http", V_activej_version))
				.include(dependency("io.activej", "activej-jmxapi", V_activej_version))
				.include(dependency("com.fasterxml.jackson.core", "jackson-databind", "2.17.0"))
				.include(dependency("com.squareup.okhttp3", "okhttp", "4.12.0"))
				.include(dependency("io.undertow", "undertow-servlet", "2.3.12.Final"))
				.include(dependency("org.apache.commons", "commons-lang3", "3.14.0"))
				.include(dependency("net.java.dev.jna", "jna", "5.14.0"))
				.include(dependency("io.reactivex.rxjava3", "rxjava", "3.1.8"))
				.include(dependency("me.friwi", "jcefmaven", "122.1.10"))
				.include(dependency("org.reactivestreams", "reactive-streams", "1.0.4"))
				.include(dependency("commons-codec", "commons-codec", "1.17.0"))
				.include(dependency("org.slf4j", "slf4j-simple", "2.0.13"))
				.include(dependency("org.slf4j", "slf4j-api", "2.0.13"))
				.include(dependency("org.jdeferred.v2", "jdeferred-core", V_jdeferred_version))
				.include(dependency("org.eclipse.jdt", "org.eclipse.jdt.annotation", "2.3.0"))
				.include(dependency("org.jetbrains", "annotations", "24.1.0"))
				.include(dependency("antlr", "antlr", "2.7.7"))
				.include(dependency("commons-cli", "commons-cli", "1.8.0"))
				.include(dependency("com.github.spotbugs", "spotbugs-annotations", "4.8.3"))
				.include(dependency("com.google.guava", "guava", V_guava_version))
				.include(dependency("tripleo.buffers", "buffers-v1", V_buffers_version))
				.include(dependency("tripleo.util.range", "range-v1", V_range_version));
		scope(test)
				.include(dependency("org.jsoup", "jsoup", version(1, 17, 2)))

				.include(dependency("org.junit.jupiter", "junit-jupiter", version(5, 10, 2)))
				.include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1, 10, 2)))

				.include(dependency("ch.qos.logback", "logback-classic", "1.5.3"))
				.include(dependency("org.easymock", "easymock", "5.2.0"))
				.include(dependency("junit", "junit", "4.13.2"));

		scope(standalone)
				.include(dependency("org.eclipse.jetty.ee10", "jetty-ee10", version(12, 0, 6)))
				.include(dependency("org.eclipse.jetty.ee10", "jetty-ee10-servlet", version(12, 0, 6)))
				.include(dependency("org.slf4j", "slf4j-simple", version(2, 0, 12)));

		precompileOperation()
				.templateTypes(HTML);

		var core_directory = new File(workDirectory(), "src");
		var mal_directory = new File(workDirectory(), "src");
		var undertow_directory = new File(workDirectory(), "src");
		var cef_directory = new File(workDirectory(), "src");

		compileOperation()
				.buildMainDirectory(Path.of(core_directory.toString(), "main", "java").toFile())
				.buildMainDirectory(Path.of(mal_directory.toString(), "java").toFile())
				.buildMainDirectory(Path.of(undertow_directory.toString(), "java").toFile())
				.buildMainDirectory(Path.of(cef_directory.toString(), "java").toFile())

				.buildTestDirectory(Path.of(core_directory.toString(), "test", "java").toFile());
	}

	public static void main(String[] args) {
		new RifersBuild().start(args);
	}
}
