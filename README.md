# Run producer
    make run-producer

    send test key1:message:10

# Run consumer
    make run-consumer queue=test concurrencyFactor=10 name=con1