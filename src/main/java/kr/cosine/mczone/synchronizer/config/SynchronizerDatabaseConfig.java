package kr.cosine.mczone.synchronizer.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SynchronizerDatabaseConfig {
    public static ForgeConfigSpec.Builder builder;

    public static ForgeConfigSpec spec;

    public static ForgeConfigSpec.ConfigValue<String> host;

    public static ForgeConfigSpec.ConfigValue<Integer> port;

    public static ForgeConfigSpec.ConfigValue<String> user;

    public static ForgeConfigSpec.ConfigValue<String> password;

    public static ForgeConfigSpec.ConfigValue<String> database;

    public static ForgeConfigSpec.ConfigValue<Integer> maximumPoolSize;

    static {
        builder = new ForgeConfigSpec.Builder();
        builder.push("database");

        host = builder.define("host", "localhost");

        port = builder.define("port", 3306);

        user = builder.define("user", "user");

        password = builder.define("password", "password");

        database = builder.define("database", "minecraft");

        maximumPoolSize = builder.define("maximum-pool-size", 10);

        builder.pop();
        spec = builder.build();
    }
}
