/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 23th 2014
 * 
 * 
 *       This file is part of Composition.
 *
 *       Composition is free software: you can redistribute it and/or modify it
 *       under the terms of the GNU General Public License as published by the
 *       Free Software Foundation, either version 3 of the License, or (at your
 *       option) any later version.
 * 
 *       Composition is distributed in the hope that it will be useful, but
 *       WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *       General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License along
 *       with Composition. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package es.uma.lcc.composition.parameterized_dsls;

import org.eclipse.core.resources.IFile;

public class ParameterizedDSL {
	private IFile metamodel, gcs, behavior, bindings;
	private String alias;
	
	
	public IFile getMetamodel() {
		return metamodel;
	}
	public void setMetamodel(IFile metamodel) {
		this.metamodel = metamodel;
	}
	public IFile getGcs() {
		return gcs;
	}
	public void setGcs(IFile gcs) {
		this.gcs = gcs;
	}
	public IFile getBehavior() {
		return behavior;
	}
	public void setBehavior(IFile behavior) {
		this.behavior = behavior;
	}
	public IFile getBindings() {
	  return bindings;
  }
	public void setBindings(IFile bindings) {
	  this.bindings = bindings;
  }
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public boolean isCompleted() {
		return this.metamodel != null && this.gcs != null && this.behavior != null && this.alias != null && !this.alias.equals("") && this.bindings != null;
	}
}
