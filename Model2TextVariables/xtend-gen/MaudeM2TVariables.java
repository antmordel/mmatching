import Maude.Condition;
import Maude.Constant;
import Maude.Equation;
import Maude.ImportationMode;
import Maude.MaudePackage;
import Maude.MaudeSpec;
import Maude.MaudeTopEl;
import Maude.ModElement;
import Maude.ModExpression;
import Maude.ModImportation;
import Maude.Module;
import Maude.ModuleIdModExp;
import Maude.Operation;
import Maude.RecTerm;
import Maude.SModule;
import Maude.Sort;
import Maude.SubsortRel;
import Maude.Term;
import Maude.Type;
import Maude.Variable;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;

@SuppressWarnings("all")
public class MaudeM2TVariables {
  private final boolean PRETTY_PRINT = true;
  
  private final int PRETTY_LINE_LIMIT = 150;
  
  public static void main(final String[] args) {
    final long iniTime = System.currentTimeMillis();
    MaudeM2TVariables _maudeM2TVariables = new MaudeM2TVariables();
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("outCode.maude");
    String _generate = _maudeM2TVariables.generate("outMaude.xmi", _newArrayList, "DEVSMM");
    InputOutput.<String>println(_generate);
    long _currentTimeMillis = System.currentTimeMillis();
    long _minus = (_currentTimeMillis - iniTime);
    double _divide = (_minus / 1000.0);
    String _plus = ("Code generation time: " + Double.valueOf(_divide));
    String _plus_1 = (_plus + "s.");
    InputOutput.<String>println(_plus_1);
  }
  
  public String generate(final String model, final List<String> output, final String moduleName) {
    this.doEMFSetup();
    final ResourceSetImpl resourceSet = new ResourceSetImpl();
    EPackage.Registry _packageRegistry = resourceSet.getPackageRegistry();
    _packageRegistry.put(MaudePackage.eNS_URI, MaudePackage.eINSTANCE);
    URI _createURI = URI.createURI(model);
    final Resource resource = resourceSet.getResource(_createURI, true);
    String out = "";
    EList<EObject> _contents = resource.getContents();
    Iterable<MaudeSpec> _filter = Iterables.<MaudeSpec>filter(_contents, MaudeSpec.class);
    for (final MaudeSpec maudespec : _filter) {
      String _out = out;
      CharSequence _generateCode = this.generateCode(maudespec, moduleName);
      String _string = _generateCode.toString();
      String _prettyPrint = this.prettyPrint(_string);
      out = (_out + _prettyPrint);
    }
    return out;
  }
  
  public String prettyPrint(final String string) {
    String res = string;
    boolean changes = true;
    if (this.PRETTY_PRINT) {
      while (changes) {
        {
          changes = false;
          String[] _split = res.split("\n");
          for (final String l : _split) {
            int _length = l.length();
            boolean _greaterThan = (_length > this.PRETTY_LINE_LIMIT);
            if (_greaterThan) {
              changes = true;
              int _indexOf = l.indexOf(">");
              boolean _lessEqualsThan = (_indexOf <= this.PRETTY_LINE_LIMIT);
              if (_lessEqualsThan) {
                int _indexOf_1 = l.indexOf(">");
                int _plus = (_indexOf_1 + 1);
                String _substring = l.substring(0, _plus);
                String _plus_1 = (_substring + "\n");
                int _indexOf_2 = l.indexOf("<");
                String _generateSpaces = this.generateSpaces(_indexOf_2);
                String _plus_2 = (_plus_1 + _generateSpaces);
                int _indexOf_3 = l.indexOf(">");
                int _plus_3 = (_indexOf_3 + 1);
                String _substring_1 = l.substring(_plus_3);
                String _plus_4 = (_plus_2 + _substring_1);
                String _replace = res.replace(l, _plus_4);
                res = _replace;
              } else {
                int _indexOf_4 = l.indexOf(" ", 100);
                String _substring_2 = l.substring(0, _indexOf_4);
                String _plus_5 = (_substring_2 + "\n");
                int _indexOf_5 = l.indexOf(" ", 100);
                String _generateSpaces_1 = this.generateSpaces(_indexOf_5);
                String _plus_6 = (_plus_5 + _generateSpaces_1);
                int _indexOf_6 = l.indexOf(" ", 100);
                int _plus_7 = (_indexOf_6 + 1);
                String _substring_3 = l.substring(_plus_7);
                String _plus_8 = (_plus_6 + _substring_3);
                String _replace_1 = res.replace(l, _plus_8);
                res = _replace_1;
              }
            }
          }
        }
      }
      String[] _split = string.split("\n");
      for (final String l : _split) {
        int _indexOf = l.indexOf("rl");
        boolean _startsWith = l.startsWith("rl", _indexOf);
        if (_startsWith) {
          String _replace = res.replace(l, ("\n" + l));
          res = _replace;
        }
      }
    }
    return res;
  }
  
  public String generateSpaces(final int i) {
    String res = "";
    if ((i < 30)) {
      IntegerRange _upTo = new IntegerRange(0, (i - 2));
      for (final Integer j : _upTo) {
        res = (res + " ");
      }
    } else {
      res = "          ";
    }
    return res;
  }
  
  public String firstSpaces(final String string) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    String[] _split = string.split("(?!^)");
    for (final String ch : _split) {
      boolean _equals = ch.equals(" ");
      if (_equals) {
        InputOutput.<String>println("entra");
        res = (res + " ");
      }
    }
    return res;
  }
  
  public Object doEMFSetup() {
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    return _extensionToFactoryMap.put(
      "xmi", _xMIResourceFactoryImpl);
  }
  
  public CharSequence generateCode(final MaudeSpec spec, final String moduleName) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _header = this.header();
    _builder.append(_header, "");
    _builder.newLineIfNotEmpty();
    _builder.append("mod RUN-MMATCHING is");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("pr MMATCHING .");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("pr ");
    _builder.append(moduleName, "  ");
    _builder.append(" .");
    _builder.newLineIfNotEmpty();
    _builder.append("endm");
    _builder.newLine();
    _builder.newLine();
    _builder.append("red mmatch(\'RUN-MMATCHING");
    _builder.newLine();
    _builder.append("  ");
    EList<MaudeTopEl> _els = spec.getEls();
    Iterable<SModule> _filter = Iterables.<SModule>filter(_els, SModule.class);
    SModule _get = ((SModule[])Conversions.unwrapArray(_filter, SModule.class))[0];
    EList<ModElement> _els_1 = _get.getEls();
    Iterable<Equation> _filter_1 = Iterables.<Equation>filter(_els_1, Equation.class);
    Equation _get_1 = ((Equation[])Conversions.unwrapArray(_filter_1, Equation.class))[0];
    Term _rhs = ((Equation) _get_1).getRhs();
    CharSequence _printTerm = this.printTerm(_rhs);
    _builder.append(_printTerm, "  ");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase, "  ");
    _builder.append(") .");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    _builder.append("---- Generated code programmatically using MMatching ----");
    _builder.newLine();
    _builder.append("---- @date   ");
    Date _date = new Date();
    _builder.append(_date, "");
    _builder.append("           ----");
    _builder.newLineIfNotEmpty();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printEqs(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Equation> _filter = Iterables.<Equation>filter(_els, Equation.class);
      for(final Equation eq : _filter) {
        {
          EList<Condition> _conds = eq.getConds();
          int _size = _conds.size();
          boolean _equals = (_size == 0);
          if (_equals) {
            _builder.append("eq ");
            Term _lhs = eq.getLhs();
            CharSequence _printTerm = this.printTerm(_lhs);
            _builder.append(_printTerm, "");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("= ");
            Term _rhs = eq.getRhs();
            CharSequence _printTerm_1 = this.printTerm(_rhs);
            _builder.append(_printTerm_1, "  ");
            _builder.append(" ");
            EList<String> _atts = eq.getAtts();
            CharSequence _printAtts = this.printAtts(_atts);
            _builder.append(_printAtts, "  ");
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("ceq ");
            Term _lhs_1 = eq.getLhs();
            CharSequence _printTerm_2 = this.printTerm(_lhs_1);
            _builder.append(_printTerm_2, "");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("= ");
            Term _rhs_1 = eq.getRhs();
            Object _printRecTermTopLevel = this.printRecTermTopLevel(_rhs_1);
            _builder.append(_printRecTermTopLevel, "  ");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("if ");
            EList<Condition> _conds_1 = eq.getConds();
            CharSequence _printConds = this.printConds(_conds_1);
            _builder.append(_printConds, "  ");
            _builder.append(" ");
            EList<String> _atts_1 = eq.getAtts();
            CharSequence _printAtts_1 = this.printAtts(_atts_1);
            _builder.append(_printAtts_1, "  ");
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printAtts(final EList<String> atts) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final String a : atts) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("[", "");
        } else {
          _builder.appendImmediate(" ", "");
        }
        _builder.append(a, "");
      }
      if (_hasElements) {
        _builder.append("] ", "");
      }
    }
    return _builder;
  }
  
  public CharSequence printSorts(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Sort> _filter = Iterables.<Sort>filter(_els, Sort.class);
      for(final Sort s : _filter) {
        _builder.append("sort ");
        String _name = s.getName();
        _builder.append(_name, "");
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printSubSorts(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<SubsortRel> _filter = Iterables.<SubsortRel>filter(_els, SubsortRel.class);
      for(final SubsortRel ss : _filter) {
        _builder.append("subsort");
        {
          EList<Sort> _subsorts = ss.getSubsorts();
          for(final Sort subs : _subsorts) {
            _builder.append(" ");
            String _name = subs.getName();
            _builder.append(_name, "");
          }
        }
        _builder.append(" <");
        {
          EList<Sort> _supersorts = ss.getSupersorts();
          for(final Sort supers : _supersorts) {
            _builder.append(" ");
            String _name_1 = supers.getName();
            _builder.append(_name_1, "");
          }
        }
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printOps(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Operation> _filter = Iterables.<Operation>filter(_els, Operation.class);
      for(final Operation op : _filter) {
        _builder.append("op ");
        String _name = op.getName();
        _builder.append(_name, "");
        _builder.append(" :");
        {
          EList<Type> _arity = op.getArity();
          for(final Type a : _arity) {
            _builder.append(" ");
            String _name_1 = a.getName();
            _builder.append(_name_1, "");
          }
        }
        _builder.append(" -> ");
        Type _coarity = op.getCoarity();
        String _name_2 = _coarity.getName();
        _builder.append(_name_2, "");
        _builder.append(" ");
        {
          EList<String> _atts = op.getAtts();
          boolean _hasElements = false;
          for(final String att : _atts) {
            if (!_hasElements) {
              _hasElements = true;
              _builder.append("[", "");
            } else {
              _builder.appendImmediate(" ", "");
            }
            _builder.append(att, "");
          }
          if (_hasElements) {
            _builder.append("]", "");
          }
        }
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printModuleImportations(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<ModImportation> _filter = Iterables.<ModImportation>filter(_els, ModImportation.class);
      for(final ModImportation imp : _filter) {
        {
          ImportationMode _mode = imp.getMode();
          boolean _equals = Objects.equal(_mode, ImportationMode.PROTECTING);
          if (_equals) {
            _builder.append("pr ");
          } else {
            ImportationMode _mode_1 = imp.getMode();
            boolean _equals_1 = Objects.equal(_mode_1, ImportationMode.INCLUDING);
            if (_equals_1) {
              _builder.newLineIfNotEmpty();
              _builder.append("inc ");
            } else {
              _builder.newLineIfNotEmpty();
              _builder.append("ex ");
            }
          }
        }
        ModExpression _imports = imp.getImports();
        CharSequence _printImportRelation = this.printImportRelation(_imports);
        _builder.append(_printImportRelation, "");
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printTerm(final Term term) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((term instanceof Constant)) {
        CharSequence _printConstant = this.printConstant(((Constant) term));
        _builder.append(_printConstant, "");
      } else {
        if ((term instanceof Variable)) {
          _builder.newLineIfNotEmpty();
          CharSequence _printVariable = this.printVariable(((Variable) term));
          _builder.append(_printVariable, "");
        } else {
          _builder.newLineIfNotEmpty();
          String _printRecTerm = this.printRecTerm(((RecTerm) term));
          _builder.append(_printRecTerm, "");
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printImportRelation(final ModExpression exp) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((exp instanceof ModuleIdModExp)) {
        Module _module = ((ModuleIdModExp)exp).getModule();
        String _name = _module.getName();
        _builder.append(_name, "");
      }
    }
    return _builder;
  }
  
  public Object printRecTermTopLevel(final Term term) {
    Object _xifexpression = null;
    if ((term instanceof RecTerm)) {
      _xifexpression = this.printRecTerm(((RecTerm) term));
    } else {
      CharSequence _xifexpression_1 = null;
      if ((term instanceof Constant)) {
        _xifexpression_1 = this.printConstant(((Constant) term));
      } else {
        _xifexpression_1 = this.printVariable(((Variable) term));
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String printRecTerm(final RecTerm t) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    String _op = t.getOp();
    boolean _contains = _op.contains("_");
    if (_contains) {
      int i = 0;
      String _op_1 = t.getOp();
      char[] _charArray = _op_1.toCharArray();
      for (final char ch : _charArray) {
        String _valueOf = String.valueOf(ch);
        boolean _equals = _valueOf.equals(" ");
        boolean _not = (!_equals);
        if (_not) {
          String _valueOf_1 = String.valueOf(ch);
          boolean _equals_1 = _valueOf_1.equals("_");
          if (_equals_1) {
            EList<Term> _args = t.getArgs();
            int _size = _args.size();
            boolean _greaterThan = (_size > i);
            if (_greaterThan) {
              EList<Term> _args_1 = t.getArgs();
              Term _get = _args_1.get(i);
              Object _printRecTermTopLevel = this.printRecTermTopLevel(_get);
              String _plus = ((res + " ") + _printRecTermTopLevel);
              String _plus_1 = (_plus + " ");
              res = _plus_1;
              i = (i + 1);
            }
          } else {
            res = (res + Character.valueOf(ch));
          }
        }
      }
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      String _op_2 = t.getOp();
      _builder_1.append(_op_2, "");
      {
        EList<Term> _args_2 = t.getArgs();
        boolean _hasElements = false;
        for(final Term te : _args_2) {
          if (!_hasElements) {
            _hasElements = true;
            _builder_1.append("(", "");
          } else {
            _builder_1.appendImmediate(",", "");
          }
          Object _printRecTermTopLevel_1 = this.printRecTermTopLevel(te);
          _builder_1.append(_printRecTermTopLevel_1, "");
        }
        if (_hasElements) {
          _builder_1.append(")", "");
        }
      }
      res = _builder_1.toString();
    }
    return this.cleanSpaces(res);
  }
  
  public String cleanSpaces(final String string) {
    String res = string;
    while ((res.indexOf("  ") != (-1))) {
      String _replaceAll = res.replaceAll("  ", " ");
      res = _replaceAll;
    }
    return res;
  }
  
  public CharSequence printConstant(final Constant c) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = c.getOp();
    _builder.append(_op, "");
    return _builder;
  }
  
  public CharSequence printVariable(final Variable v) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Type _type = v.getType();
      boolean _notEquals = (!Objects.equal(_type, null));
      if (_notEquals) {
        String _name = v.getName();
        _builder.append(_name, "");
        _builder.append(":");
        Type _type_1 = v.getType();
        String _name_1 = _type_1.getName();
        _builder.append(_name_1, "");
      } else {
        String _name_2 = v.getName();
        _builder.append(_name_2, "");
      }
    }
    return _builder;
  }
  
  public CharSequence printConds(final List<Condition> list) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
}
