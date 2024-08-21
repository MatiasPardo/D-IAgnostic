package org.diagnostic.infraestructure.repositories;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.diagnostic.infraestructure.repositories.interfaces.IAnalyticsRepository;

@Repository
@Slf4j
public class AnalyticsRepository implements IAnalyticsRepository {

    /*
    Jedis jedis;

    private final IOrderRepository orderRepository;

    private final IUserRepository userRepository;


    @Autowired
    public AnalyticsRepository(@Value("${redis.host}") String redisHost,
                               @Value("${redis.port}") int redisPort,
                               @Value("${redis.user}") String redisUser,
                               @Value("${redis.pass}") String redisPassword,
                               IOrderRepository orderRepository,
                               IUserRepository userRepository) {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user(redisUser)
                .password(redisPassword)
                .build();
        jedis = new Jedis(redisHost, redisPort, config);

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        initializeCounters();
    }
*/
    @Override
    public void addUser() {
        log.info("usuario agregado");
        //jedis.incr("users");
    }

    @Override
    public void addOrder() {
        log.info("pedido agregado");
        //jedis.incr("orders");
    }

    @Override
    public long countUsers() {
        String users = "1";//jedis.get("users");
        return Long.parseLong(users);
    }

    @Override
    public long countOrders() {
        String orders = "1";//jedis.get("orders");
        return Long.parseLong(orders);
    }
/*
    private void initializeCounters() {
        var a = userRepository.toString();

        var usersCount = userRepository.countUserUnique().toString();
        var ordersCount = orderRepository.count().toString();

        jedis.set("users", usersCount);
        jedis.set("orders", ordersCount);
    }
*/
}
