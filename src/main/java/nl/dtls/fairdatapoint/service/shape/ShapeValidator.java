/**
 * The MIT License
 * Copyright © 2017 DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.dtls.fairdatapoint.service.shape;

import nl.dtls.fairdatapoint.api.dto.shape.ShapeChangeDTO;
import nl.dtls.fairdatapoint.entity.exception.ValidationException;
import nl.dtls.fairdatapoint.service.rdf.ShaclValidator;
import nl.dtls.fairdatapoint.util.RdfIOUtil;
import org.eclipse.rdf4j.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShapeValidator {
	@Autowired
	private ShaclValidator validator;
	
    public void validate(ShapeChangeDTO reqDto) {
        // Try to parse SHACL definition
    	final Model data;
        try {
            data = RdfIOUtil.read(reqDto.getDefinition(), "");
        } catch (ValidationException e) {
            throw new ValidationException("Unable to read SHACL definition");
        }
        
        var shsh = RdfIOUtil.readFile("/shapes/shacl-shacl.ttl", "http://www.w3.org/ns/shacl-shacl#");
        
        // using a dummy base uri
        validator.validate(shsh, data, "http://fairdatapoint.org/");
    }

}
