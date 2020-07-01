package com.kyra.common.proxy;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

import java.util.Objects;

public class ProxyHelper {
  final ServiceDiscovery discovery;

  public ProxyHelper(ServiceDiscovery discovery) {
    this.discovery = discovery;
  }

  public Future<AccountProxy> accountProxy() {
    Future<AccountProxy> future = Promise.<AccountProxy>promise().future();
    discovery.getRecord(r -> r.getName().equals(AccountProxy.SERVICE_NAME), ar -> {
      if (ar.failed() || Objects.isNull(ar.result())) future.handle(Future.failedFuture(ar.cause()));
      else {
        ServiceReference reference = discovery.getReference(ar.result());
        future.handle(Future.succeededFuture(reference.getAs(AccountProxy.class)));
      }
    });

    return future;
  }

}
