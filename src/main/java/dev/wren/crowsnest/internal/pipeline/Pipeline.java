package dev.wren.crowsnest.internal.pipeline;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.valkyrienskies.core.api.ships.LoadedShip;

import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {

    private final List<OperationDefinition<?, ?>> nodes;

    public Pipeline() {
        this.nodes = new ArrayList<>();
    }

    public void addOperation(OperationDefinition<?, ?> node) {
        nodes.add(node);
    }

    public List<OperationDefinition<?, ?>> getOperations() {
        return nodes;
    }


    public static Pipeline parse(String input) throws CommandSyntaxException {
        PipelineReader reader = new PipelineReader(input);
        System.out.println(reader.content());
        Pipeline pipeline = new Pipeline();
        Class<?> currentType = LoadedShip.class;


        while (reader.canRead()) {
            String opName = reader.nextString();
            OperationDefinition<?, ?> operation = OperationRegistry.getOperation(currentType, opName);
            if (operation == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create(opName);

            operation.populateArgs(reader);
            pipeline.addOperation(operation);

            currentType = TypeBridgeRegistry.getBridge(operation.returnType()).to();
        }

        return pipeline;
    }

    public ImmutablePipeline immutable() {
        return new ImmutablePipeline(this);
    }
}