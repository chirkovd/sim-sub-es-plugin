package org.elasticsearch.plugin.index.tokenizers;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.plugin.services.SimSubService;
import org.elasticsearch.plugin.utils.TokenizerType;

import java.io.IOException;

/**
 * Project: sim-sub-es-plugin
 * Description:
 * Date: 7/22/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractSimSubTokenizer extends Tokenizer {

    private final TokenizerType type;
    private final SimSubService simSubService;

    private boolean done;

    private String fingerprint;
    private int position;

    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final TypeAttribute typeAttribute = addAttribute(TypeAttribute.class);

    protected AbstractSimSubTokenizer(TokenizerType type, SimSubService simSubService) {
        this.type = type;
        this.simSubService = simSubService;
        this.done = false;
    }

    @Override
    public boolean incrementToken() throws IOException {
        clearAttributes();

        if (!done) {
            if (initFingerPrints()) {
                done = true;
            } else {
                return false;
            }
        }

        if (this.position < this.fingerprint.length()) {
            String token = this.fingerprint.substring(this.position, this.position + 1);
            if (!"0".equals(token)) {
                this.posIncrAtt.setPositionIncrement(1);
                this.charTermAttribute.append(String.valueOf(this.position));
                this.typeAttribute.setType(StandardTokenizer.TOKEN_TYPES[StandardTokenizer.NUM]);
            }
            this.offsetAtt.setOffset(correctOffset(this.position), correctOffset(this.position + 1));
            this.position++;
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void reset() throws IOException {
        super.reset();
        this.done = false;
        this.position = 0;
    }

    private boolean initFingerPrints() {
        if (this.fingerprint == null || this.fingerprint.isEmpty()) {
            try {
                String value = IOUtils.toString(input);
                if (value != null && !value.isEmpty()) {
                    this.fingerprint = simSubService.fingerprints(value, type);
                }
            } catch (IOException e) {
                return false;
            }
        }
        return this.fingerprint != null && !this.fingerprint.isEmpty();
    }
}
