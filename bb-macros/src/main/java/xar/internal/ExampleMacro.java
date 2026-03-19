/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package xar.internal;

import javax.inject.Named;

import java.util.List;
import javax.inject.Singleton;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.block.LinkBlock;
import org.xwiki.rendering.block.ParagraphBlock;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.listener.reference.ResourceType;
import xar.ExampleMacroParameters;
import org.xwiki.rendering.transformation.MacroTransformationContext;

/**
 * Example Macro.
 */
@Component
@Singleton
@Named("simple-example")
public class ExampleMacro extends AbstractMacro<ExampleMacroParameters> {
	/**
	 * The description of the macro.
	 */
	private static final String DESCRIPTION = "Simple Example Macro";

	/**
	 * Create and initialize the descriptor of the macro.
	 */
	public ExampleMacro() {
		super("Simple Example", DESCRIPTION, ExampleMacroParameters.class);
	}

	@Override
	public List<Block> execute(ExampleMacroParameters parameters, String content, MacroTransformationContext context)
			throws MacroExecutionException {
		List<Block> result;
		String bbgredir = "bbg://screens/" + parameters.getFunction();
		ResourceReference bbfunc_link = new ResourceReference(bbgredir, ResourceType.URL);

		List<Block> bb_func_label = List.of(new WordBlock("{" + parameters.getFunction() + " <GO>}"));
		LinkBlock bb_link = new LinkBlock(bb_func_label, bbfunc_link, false);
		bb_link.setParameter("title", "terminal link");
		bb_link.setParameter("target", "_self");
		bb_link.setParameter("rel", "nofollow noopener");

		//List<Block> wordBlockAsList = List.of(new WordBlock(parameters.getFunction()));
		//List<Block> wordBlockAsList = List.of(new WordBlock(parameters.getFunction()));

		// Handle both inline mode and standalone mode.
		if (context.isInline()) {
		    result = List.of(bb_link);
		} else {
			// Wrap the result in a Paragraph Block since a WordBlock is an inline element, and it needs to be
			// inside a standalone block.
		    result = List.of(new ParagraphBlock(List.of(bb_link)));
		}

		return result;
	}

	@Override
	public boolean supportsInlineMode() {
		return true;
	}
}
