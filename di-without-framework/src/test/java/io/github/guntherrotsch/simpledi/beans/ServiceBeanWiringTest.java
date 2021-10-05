package io.github.guntherrotsch.simpledi.beans;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import io.github.guntherrotsch.simpledi.RequestContext;
import io.github.guntherrotsch.simpledi.RequestScoped;

class ServiceBeanWiringTest {

	class ServiceBean {
		final Supplier<FooBean> fooBean;
		final Supplier<BarBean> barBean;

		ServiceBean(Supplier<FooBean> fooBean, Supplier<BarBean> barBean) {
			this.fooBean = fooBean;
			this.barBean = barBean;
		}

		List<String> process(boolean fooFirst) {
			List<String> result = null;
			try (RequestContext context = RequestScoped.getContext()) {
				if (fooFirst) {
					fooBean.get().sayHello();
					Thread.sleep(500);
					barBean.get().sayHello();
				} else {
					barBean.get().sayHello();
					Thread.sleep(500);
					fooBean.get().sayHello();
				}
				result = MessageCollector.supplier().get().getMessages();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	ServiceBean testee = new ServiceBean(FooBean.supplier(Config.supplier(), MessageCollector.supplier()),
			BarBean.supplier(Config.supplier(), MessageCollector.supplier()));

	@Test
	void test() throws InterruptedException {
		CountDownLatch startLatch = new CountDownLatch(2);
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(() -> {
			startLatch.countDown();
			try {
				startLatch.await();
				List<String> actual = testee.process(true);
				System.out.println(actual);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		executorService.execute(() -> {
			startLatch.countDown();
			try {
				startLatch.await();
				List<String> actual = testee.process(false);
				System.out.println(actual);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		executorService.awaitTermination(5, TimeUnit.SECONDS);
	}
}
