/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.elijah.stages.gen_generic;

import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;

/**
 * Move this to Mir or Lir layer ASAP
 * <p>
 * Created 11/28/21 4:45 PM
 */
public interface ICodeRegistrar {
    void registerNamespace(GeneratedNamespace aNamespace);

    void registerClass(GeneratedClass aClass);

    void registerFunction(BaseGeneratedFunction aFunction);
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
