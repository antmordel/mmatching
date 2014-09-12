package codeGeneration

import java.io.BufferedWriter
import java.io.FileWriter
import Maude.Condition
import Maude.Constant
import Maude.Equation
import Maude.ImportationMode
import Maude.MaudePackage
import Maude.MaudeSpec
import Maude.ModExpression
import Maude.ModImportation
import Maude.Module
import Maude.ModuleIdModExp
import Maude.Operation
import Maude.RecTerm
import Maude.Rule
import Maude.SModule
import Maude.Sort
import Maude.SubsortRel
import Maude.Term
import Maude.Variable
import java.util.Date
import java.util.List
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import Maude.MatchingCond
import Maude.BooleanCond

class MaudeM2T {
  private val PRETTY_PRINT = true
  private val PRETTY_LINE_LIMIT = 150
  
  public def MaudeM2T(){}
  
  def static void main(String[] args) {
    val iniTime = System.currentTimeMillis
    new MaudeM2T().generate("out_maude.xmi", newArrayList("outCode.maude"))
    println("Code generation time: " + (System.currentTimeMillis - iniTime) / 1000.0 + "s.")
  }
  
  def void generate(String model, List<String> output) {
    doEMFSetup
    val resourceSet = new ResourceSetImpl

    // Register Maude Metamodel
    resourceSet.getPackageRegistry().put(MaudePackage.eNS_URI, MaudePackage.eINSTANCE)

    // Load model
    val resource = resourceSet.getResource(URI.createURI(model), true)
    val cont = 0
    for (maudespec : resource.contents.filter(typeof(MaudeSpec))) {

      // A file is created for each Maude Specification
      val fw = new FileWriter(output.get(cont))
      val bw = new BufferedWriter(fw)
      bw.write(prettyPrint(generateCode(maudespec).toString))

      // TODO remove
      println(prettyPrint(generateCode(maudespec).toString))
      bw.close
      fw.close
     }
  }
  
  def prettyPrint(String string) {
    var res = string
    var changes = true
    if (PRETTY_PRINT){
      while(changes){
        changes = false
        for(l : res.split("\n")){
         if(l.length > PRETTY_LINE_LIMIT){
           changes = true
           if(l.indexOf(">") <= PRETTY_LINE_LIMIT){
             res = res.replace(l,
               l.substring(0, l.indexOf(">")+1)+"\n"+generateSpaces(l.indexOf("<"))+l.substring(l.indexOf(">")+1)
             )
           } else {
             res = res.replace(l,
               l.substring(0, l.indexOf(" ", 100))+"\n"+generateSpaces(l.indexOf(" ", 100))+l.substring(l.indexOf(" ", 100)+1)
             )
           }
         }
        }
      }
      for(l : string.split("\n")){
          if(l.startsWith("rl",l.indexOf('rl'))){
            res = res.replace(l,"\n"+l)
         }
      }
    }
    return res
  }
  
  def generateSpaces(int i) {
    var res = ""
    if(i < 30){
      for(j : 0..i-2){
        res = res + " "
      }
    } else {
      res = "          "
    }
    return res
  }
  

  
  def firstSpaces(String string) {
    var res = ''''''
    for(ch : string.split("(?!^)")){
      if(ch.equals(' ')){
        println("entra")
        res = res + " "
      }
    }
    return res
  }

  def doEMFSetup() {
    Resource.Factory.Registry::INSTANCE.extensionToFactoryMap.put(
      "xmi",
      new XMIResourceFactoryImpl
    );
  }

  def generateCode(MaudeSpec spec) '''
    «header()»
    «FOR smod : spec.els.filter(typeof(SModule))»
      mod «smod.name» is
        «printModule(smod)»
      endm ---- system module «smod.name»

    «ENDFOR»
  '''
  
  def printModule(Module mod) '''
    «printModuleImportations(mod)»
    «IF mod.els.filter(typeof(ModImportation)).size > 0»
      
    «ENDIF»
    «printSorts(mod)»
    «IF mod.els.filter(typeof(Sort)).size > 0»
      
    «ENDIF»
    «printSubSorts(mod)»
    «IF mod.els.filter(typeof(SubsortRel)).size > 0»
      
    «ENDIF»
    «printOps(mod)»
    «IF mod.els.filter(typeof(Operation)).size > 0»
      
    «ENDIF»
    «printEqs(mod)»
  '''

  def header() '''
    ---- ----------------------------------------------- ----
    ---- Generated code programmatically using MMatching ----
    ---- @date   «new Date»           ----
    ---- ----------------------------------------------- ----

  '''
  
  def printEqs(Module mod) '''
    «FOR eq : mod.els.filter(typeof(Equation))»
      «IF eq.conds.size == 0»
        eq «printTerm(eq.lhs)»
          = «printTerm(eq.rhs)» «printAtts(eq.atts)».
      «ELSE»
        ceq «printTerm(eq.lhs)»
          = «printRecTermTopLevel(eq.rhs)»
          if «printConds(eq.conds)» «printAtts(eq.atts)».
      «ENDIF»
    «ENDFOR»
  '''
  
  def printAtts(EList<String> atts)'''
  «FOR a : atts BEFORE "[" SEPARATOR " " AFTER "] "»«a»«ENDFOR»'''

  def printSorts(Module mod) '''
    «FOR s : mod.els.filter(typeof(Sort))»
      sort «s.name» .
    «ENDFOR»   
  '''

  def printSubSorts(Module mod) '''
    «FOR ss : mod.els.filter(typeof(SubsortRel))»
      subsort«FOR subs : ss.subsorts» «subs.name»«ENDFOR» <«FOR supers : ss.supersorts» «supers.name»«ENDFOR» .
    «ENDFOR»
  '''

  def printOps(Module mod) '''
    «FOR op : mod.els.filter(typeof(Operation))»
      op «op.name» :«FOR a : op.arity» «a.name»«ENDFOR» -> «op.coarity.name» «FOR att : op.atts BEFORE '[' SEPARATOR ' ' AFTER ']'»«att»«ENDFOR» .
    «ENDFOR»  
  '''

  def printModuleImportations(Module mod) '''
    «FOR imp : mod.els.filter(typeof(ModImportation))»
      «IF imp.mode == ImportationMode.PROTECTING»
      pr «ELSEIF imp.mode == ImportationMode.INCLUDING»
      inc «ELSE/* extending */»
      ex «ENDIF»«printImportRelation(imp.imports)» .
    «ENDFOR»
  '''
  
  def printTerm(Term term) '''
    «IF (term instanceof Constant)»
      «printConstant(term as Constant)»«ELSEIF (term instanceof Variable)»
      «printVariable(term as Variable)»«ELSE»
      «printRecTerm((term as RecTerm))»«ENDIF»''' 

  def printImportRelation(ModExpression exp) '''
  «IF exp instanceof ModuleIdModExp»
  «exp.module.name»«ENDIF»'''

  def printRecTermTopLevel(Term term) {
    if (term instanceof RecTerm) {
      printRecTerm(term as RecTerm)
    } else if (term instanceof Constant) {
      printConstant(term as Constant)
    } else { // Variable
      printVariable(term as Variable)
    }
  }

  def printRecTerm(RecTerm t) {
    var res = ''''''
    if (t.op.contains('_')) { // mixfix
      var i = 0
      for (ch : t.op.toCharArray) {
        if (!String.valueOf(ch).equals(' ')) {
          if (String.valueOf(ch).equals('_')) {
            if(t.args.size > i){
              res = res + ' ' + printRecTermTopLevel(t.args.get(i)) + ' '
              i = i + 1
            }
          } else {
            res = res + ch
          }
        }
      }
    } else { //prefix
      res = '''«t.op»«FOR te : t.args BEFORE '(' SEPARATOR ',' AFTER ')'»«printRecTermTopLevel(te)»«ENDFOR»'''
    }
    return cleanSpaces(res)
  }
  
  def cleanSpaces(String string) {
    var res = string
    while(res.indexOf("  ") != -1){
      res = res.replaceAll("  ", " ")
    }
    return res
  }

  def printConstant(Constant c) '''«c.op»'''

  def printVariable(Variable v) '''
    «IF v.type != null»
      «v.name»:«v.type.name»«ELSE/* type == null */
      »«v.name»«ENDIF»'''

  def printConds(List<Condition> list) ''''''

}
