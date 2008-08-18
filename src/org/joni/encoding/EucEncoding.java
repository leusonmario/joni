/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package org.joni.encoding;

import org.joni.Config;

public abstract class EucEncoding extends MultiByteEncoding {

    protected EucEncoding(int minLength, int maxLength, int[]EncLen, int[][]Trans, short[]CTypeTable) {
        super(minLength, maxLength, EncLen, Trans, CTypeTable);
    }

    protected abstract boolean isLead(int c);
    
    @Override
    public int leftAdjustCharHead(byte[]bytes, int p, int end) {
        /* In this encoding mb-trail bytes doesn't mix with single bytes. */
        if (end <= p) return end;
        int p_ = end;

        while (!isLead(bytes[p_] & 0xff) && p_ > p) p_--;

        // TODO: length(bytes, p_, end) introduces regression for look-behind here:
        // /(?<=あ|いう)い/ =~ 'いうい', fix this or fix the test
        int len = length(bytes, p_, p);

        if (p_ + len > end) return p_;

        p_ += len;
        return p_ + ((end - p_) & ~1);
    }    
}
