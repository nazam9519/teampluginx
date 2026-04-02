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

import java.util.*;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.*;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.MacroTransformationContext;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.skinx.SkinExtension;
import com.xpn.xwiki.XWikiContext;
import javax.inject.Provider;
import org.slf4j.Logger;
import xar.TitleMacroParameters;
import xar.BBMacros;

@Component
@Named("bb-title")
@Singleton
public class TitleMacro extends AbstractMacro<TitleMacroParameters> {
//    @Inject
//    @Named("jsx")
//    private SkinExtension jsx;
    @Inject
    private Provider<XWikiContext> xContextProvider;
    @Inject
    private Logger log;
    @Inject
    private DocumentAccessBridge documentAccessBridge;

    public TitleMacro() {
        super("Bloomberg Title", "A Macro to Display an Overview of a Title(User Inputs and Decides)", TitleMacroParameters.class);
    }

    @Override
    public boolean supportsInlineMode() {
        return true;
    }


    @Override
    public List<Block> execute(TitleMacroParameters parameters, String content,
                               MacroTransformationContext context) throws MacroExecutionException {
        //render the heading with the background gradient
        List<Block> blocks = new ArrayList<>();
        blocks.add(injectCss());

        HeaderBlock header = new HeaderBlock(List.of(new WordBlock(
                (content != null && !content.isEmpty()) ?
                        content.trim() :
                        xContextProvider.get().getDoc().getDocumentReference().getLastSpaceReference().getName()
                )
        ),HeaderLevel.LEVEL2);
        header.setParameter("class", "title");
        log.debug("Rendering bb-title macro version {}", BBMacros.VERSION);
        blocks.add(header);

        return blocks;
    }

    private RawBlock injectCss() {
        return new RawBlock(
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + BBMacros.WEBJAR_BASE + "/css/title.css\"/>",
                Syntax.HTML_5_0
        );
    }
}
