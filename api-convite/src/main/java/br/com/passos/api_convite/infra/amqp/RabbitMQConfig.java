package br.com.passos.api_convite.infra.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${api.rabbitmq.exchange:convite.exchange}")
    private String exchangeName;

    @Value("${api.rabbitmq.queue:convite.email.enviar.queue}")
    private String queueName;

    @Value("${api.rabbitmq.routing-key:convite.email.enviar}")
    private String routingKey;

    @Bean
    public DirectExchange conviteExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue conviteEmailQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding conviteEmailBinding(Queue conviteEmailQueue, DirectExchange conviteExchange) {
        return BindingBuilder.bind(conviteEmailQueue)
                .to(conviteExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
