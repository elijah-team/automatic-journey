package tripleo.elijah;

import io.activej.csp.ChannelInput;
import io.activej.csp.ChannelOutput;
import io.activej.csp.consumer.ChannelConsumer;
import io.activej.csp.consumer.ChannelConsumers;
import io.activej.csp.dsl.WithChannelTransformer;
import io.activej.csp.process.AbstractCommunicatingProcess;
import io.activej.csp.supplier.ChannelSupplier;
import io.activej.csp.supplier.ChannelSuppliers;
import io.activej.eventloop.Eventloop;
import io.activej.promise.Promise;
import org.junit.Test;

/**
 * AsyncProcess that takes a string, sets it to upper-case and adds string's length in parentheses
 */
public final class CspExample {
    @Test
    public void oneTest() {
        final Eventloop eventloop = Eventloop.builder()
                .withCurrentThread()
                .build();

        final CspExample1 process = new CspExample1();
        ChannelSuppliers.ofValues("hello", "world", "nice", "to", "see", "you")
                .transformWith(process)
                .streamTo(ChannelConsumers.ofConsumer(System.out::println));

        eventloop.run();
    }

    public final class CspExample1 extends AbstractCommunicatingProcess implements WithChannelTransformer<CspExample, String, String> {
        private ChannelSupplier<String> input;
        private ChannelConsumer<String> output;

        @Override
        public ChannelOutput<String> getOutput() {
            final var _c = CspExample1.this;
            return new ChannelOutput<String>() {
                @Override
                public void set(final ChannelConsumer<String> output) {
                    _c.output = output;
                    if (_c.input != null && _c.output != null)
                        _c.startProcess();
                }
            };
        }

        @Override
        public ChannelInput<String> getInput() {
            final var _c = CspExample1.this;
            return new ChannelInput<String>() {
                @Override
                public Promise<Void> set(final ChannelSupplier<String> input) {
                    _c.input = input;
                    if (_c.input != null && _c.output != null) _c.startProcess();
                    return _c.getProcessCompletion();
                }
            };
        }

        @Override
        //[START REGION_1]
        protected void doProcess() {
            input.get()
                    .whenResult(data -> {
                        if (data == null) {
                            output.acceptEndOfStream()
                                    .whenResult(this::completeProcess);
                        } else {
                            data = data.toUpperCase() + '(' + data.length() + ')';

                            output.accept(data)
                                    .whenResult(this::doProcess);
                        }
                    })
                    .whenException(Throwable::printStackTrace);
        }
        //[END REGION_1]

        @Override
        protected void doClose(Exception e) {
            System.out.println("Process has been closed with exception: " + e);
            input.closeEx(e);
            output.closeEx(e);
        }
    }
}
