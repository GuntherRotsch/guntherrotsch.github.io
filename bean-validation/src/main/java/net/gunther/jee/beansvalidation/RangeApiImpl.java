package net.gunther.jee.beansvalidation;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gunther.blog.codegen.api.RangeApi;
import net.gunther.blog.codegen.models.Range;

@Path("/range")
public class RangeApiImpl implements RangeApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(RangeApiImpl.class);

	@POST
	@Consumes({ "application/json" })
	public Response newRange(@Valid Range range) {
		LOGGER.info("Range: {}", range);
		return null;
	}
}
