from confluent_kafka import Producer

producer = Producer({'bootstrap.servers': 'localhost:9093'})
def result(err, message):
	if err:
		print('error %s\n' % err)
	else:
		print('info : topic=%s, and offset=%d\n' %
		(message.topic(), message.offset()))

messages = ["hello python", "hello again"]

for msg in messages:
	producer.poll(0)
	producer.produce("sales-data",
	value=msg.encode('utf-8'), callback=result)

producer.flush()
