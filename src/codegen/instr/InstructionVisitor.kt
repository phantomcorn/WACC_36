package codegen.instr

import codegen.instr.operand2.Immediate
import codegen.instr.operand2.ImmediateChar
import codegen.instr.operand2.ImmediateOffset
import codegen.instr.operand2.ZeroOffset
import codegen.instr.operand2.RegisterOffset
import codegen.instr.operand2.PreRegisterOffset
import codegen.instr.operand2.PreImmediateOffset
import codegen.instr.operand2.ShiftOffset
import codegen.instr.register.GP
import codegen.instr.register.LR
import codegen.instr.register.PC
import codegen.instr.register.SP
import codegen.instr.loadable.Msg

interface InstructionVisitor<T> {
    fun visitInstructions(instrs: List<Instruction>): T
    fun visitTest(x: Test): T
    fun visitTestEquiv(x: TestEquiv): T
    fun visitAnd(x: And): T
    fun visitXor(x: Xor): T
    fun visitOr(x: Or): T
    fun visitAdd(x: Add): T
    fun visitSub(x: Subtract): T
    fun visitRevSub(x: ReverseSubtract): T
    fun visitMul(x: Multiply): T
    fun visitBranch(x: Branch): T
    fun visitBranchWithLink(x: BranchWithLink): T
    fun visitMove(x: Move): T
    fun visitCompare(x: Compare): T
    fun visitLoad(x: Load): T
    fun visitLoadByte(x: LoadByte): T
    fun visitGPRegister(x: GP): T
    fun visitPCRegister(x: PC): T
    fun visitLRRegister(x: LR): T
    fun visitSPRegister(x: SP): T
    fun visitImmediate(x: Immediate): T
    fun visitImmediateChar(x: ImmediateChar): T
    fun visitStore(x: Store): T
    fun visitStoreByte(x: StoreByte): T
    fun visitPush(x: Push): T
    fun visitPop(x: Pop): T
    fun visitMod(x: Mod): T
    fun visitDiv(x: Div): T
    fun visitLabel(x: Label): T
    fun loadImmediate(x: Immediate): T
    fun loadMsg(x: Msg): T
    fun visitImmediateOffset(x: ImmediateOffset): T
    fun loadImmediateOffset(x: ImmediateOffset): T
    fun visitZeroOffset(x: ZeroOffset): T
    fun loadZeroOffset(x: ZeroOffset): T
    fun visitRegisterOffset(x: RegisterOffset): T
    fun loadRegisterOffset(x: RegisterOffset): T
    fun visitPreRegisterOffset(x: PreRegisterOffset): T
    fun loadPreRegisterOffset(x: PreRegisterOffset): T
    fun visitPreImmediateOffset(x: PreImmediateOffset): T
    fun loadPreImmediateOffset(x: PreImmediateOffset): T
    fun visitShiftOffset(x: ShiftOffset): T
    fun loadShiftOffset(x: ShiftOffset): T
    fun visitSBool(x: SFlag): T
    fun visitCond(x: Cond): T
}
