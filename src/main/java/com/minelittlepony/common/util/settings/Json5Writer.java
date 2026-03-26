package com.minelittlepony.common.util.settings;

import java.io.IOException;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.Util;

public class Json5Writer extends JsonWriter {
    static boolean LOAD_FAIL;
    static final BiConsumer<JsonWriter, Integer> INVOKE_REPLACE_TOP = Util.make(() -> {
        try {
            var handle = MethodHandles.privateLookupIn(JsonWriter.class, MethodHandles.lookup()).findSpecial(JsonWriter.class, "replaceTop", MethodType.methodType(void.class, int.class), JsonWriter.class);
            return (self, top) -> {
                try {
                    handle.invoke(self, top);
                } catch (Throwable t) {}
            };
        } catch (Throwable e) {
            LOAD_FAIL = true;
            return (_, _) -> {};
        }
    });
    static final Function<JsonWriter, Integer> INVOKE_PEEK = Util.make(() -> {
        try {
            var handle = MethodHandles.privateLookupIn(JsonWriter.class, MethodHandles.lookup()).findSpecial(JsonWriter.class, "peek", MethodType.methodType(int.class), JsonWriter.class);
            return self -> {
                try {
                    return (int)handle.invoke(self);
                } catch (Throwable t) {}
                return -1;
            };
        } catch (Throwable e) {
            LOAD_FAIL = true;
            return _ -> -1;
        }
    });

    static final int EMPTY_ARRAY = 1;
    static final int NONEMPTY_ARRAY = 2;
    static final int EMPTY_OBJECT = 3;
    static final int NONEMPTY_OBJECT = 5;

    static final String INDENT = "  ";

    private final Writer out;

    private int depth;
    @Nullable
    private String deferredName;
    private boolean firstMember = true;
    private final List<String> deferredComments = new ArrayList<>();

    public Json5Writer(Writer out, Gson gson) {
        super(out);
        this.out = out;
        setIndent(INDENT);
        setHtmlSafe(gson.htmlSafe());
        setSerializeNulls(gson.serializeNulls());
        setStrictness(Strictness.LENIENT);
    }

    public Json5Writer comment(String comment) {
        deferredComments.add(comment);
        return this;
    }

    @Override
    public Json5Writer beginArray() throws IOException {
        beforeValue();
        super.beginArray();
        firstMember = true;
        depth++;
        return this;
    }

    @Override
    public Json5Writer endArray() throws IOException {
        super.endArray();
        firstMember = false;
        depth--;
        return this;
    }

    @Override
    public Json5Writer beginObject() throws IOException {
        beforeValue();
        super.beginObject();
        firstMember = true;
        depth++;
        return this;
    }

    @Override
    public Json5Writer endObject() throws IOException {
        super.endObject();
        firstMember = false;
        depth--;
        return this;
    }

    @Override
    public Json5Writer name(String name) throws IOException {
        deferredName = name;
        super.name(name);
        return this;
    }

    @Override
    public JsonWriter value(String value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer jsonValue(String value) throws IOException {
        beforeValue();
        super.jsonValue(value);
        return this;
    }

    @Override
    public Json5Writer nullValue() throws IOException {
        beforeValue();
        super.nullValue();
        return this;
    }

    @Override
    public Json5Writer value(boolean value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer value(Boolean value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer value(float value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer value(double value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer value(long value) throws IOException {
        beforeValue();
        super.value(value);
        return this;
    }

    @Override
    public Json5Writer value(Number value) throws IOException {
        beforeValue();
        super.value(value);

        return this;
    }

    protected void newline() throws IOException {
        out.write('\n');
        for (int i = 0; i < depth; i++) {
            out.write(INDENT);
        }
    }

    protected void beforeValue() throws IOException {
        if (deferredName != null) {
            deferredName = null;
            if (!deferredComments.isEmpty()) {

                if (!LOAD_FAIL) {
                    // Prevent generated json from adding commas onto the ends of comments
                    int top = INVOKE_PEEK.apply(this);
                    if (top == NONEMPTY_OBJECT || top == NONEMPTY_ARRAY) {
                        INVOKE_REPLACE_TOP.accept(this, top == NONEMPTY_OBJECT ? EMPTY_OBJECT : EMPTY_ARRAY);
                    }
                }

                if (!firstMember) {
                    out.write(',');
                }

                newline();
                out.write("/*");

                for (String comment : deferredComments) {
                    newline();
                    out.write(" * ");
                    out.write(comment);
                }
                newline();
                out.write(" */");
                deferredComments.clear();
            }
        }
        firstMember = false;
    }

}
