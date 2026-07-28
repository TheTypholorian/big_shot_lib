package net.typho.big_shot_lib.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Objects;

public class BigShotLibAgent {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach(args[0]);

        try {
            vm.loadAgent(args[1]);
        } finally {
            vm.detach();
        }
    }

    public static void premain(String args, Instrumentation inst) throws UnmodifiableClassException {
        init(inst);
    }

    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        init(inst);
    }

    public static void init(Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("MUAHAHAHAHA");
        inst.addTransformer(
                new ClassFileTransformer() {
                    @Override
                    public byte[] transform(
                            ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer
                    ) {
                        if (!Objects.equals(className, "net/minecraft/client/Minecraft")) {
                            return null;
                        }

                        var node = new ClassNode();
                        new ClassReader(classfileBuffer).accept(node, 0);

                        var insn = new InsnList();
                        insn.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        insn.add(new LdcInsnNode("Hello World!"));
                        insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                        node.methods.stream().filter(m -> m.name.equals("<init>")).findAny().orElseThrow().instructions.insert(insn);

                        var writer = new ClassWriter(0);
                        node.accept(writer);
                        return writer.toByteArray();
                    }
                },
                true
        );
        System.out.println(Arrays.asList(inst.getAllLoadedClasses()).stream().filter(c -> c.getName().startsWith("net.minecraft")).toList());
        //inst.retransformClasses(Arrays.asList(inst.getAllLoadedClasses()).stream().filter(c -> c.getName().equals("net.minecraft.client.Minecraft")).findAny().orElseThrow());
    }
}
