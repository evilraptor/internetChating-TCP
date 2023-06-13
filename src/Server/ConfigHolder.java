package Server;

public class ConfigHolder {
    private final int max_sleeper_user_life_time_in_mills = 90000;
    private final int max_count_of_giving_messages=10;//FIXME забыл добавить макс количество возвращаемых сообщеий в сервере... (чек)
    private final boolean saving_log_flag=true;

    public int getMax_count_of_giving_messages() {
        return max_count_of_giving_messages;
    }

    public int getMax_sleeper_user_life_time_in_mills() {
        return max_sleeper_user_life_time_in_mills;
    }

    public boolean isSaving_log_flag() {
        return saving_log_flag;
    }
}
