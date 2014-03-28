/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.user;

import architecture.common.util.StringUtils;

public class DomainMatcher implements java.io.Serializable {

		private Integer objectType = 0;
		
		private Long objectId = -1L;
		
		private String patten ;

		/**
		 * @param companyId
		 * @param patten
		 */
		public DomainMatcher(Integer objectType, Long objectId, String patten) {
			this.objectId = objectId;
			this.patten = patten;
		}

		/**
		 * @return objectType
		 */
		public Integer getObjectType() {
			return objectType;
		}

		/**
		 * @return objectId
		 */
		public Long getObjectId() {
			return objectId;
		}

		/**
		 * @return patten
		 */
		public String getPatten() {
			return patten;
		}		
		
		public boolean match ( String str , boolean isCaseSensitive ){
			if( isCaseSensitive )
				return StringUtils.equalsIgnoreCase(patten, str);
			else
				return StringUtils.equalsIgnoreCase(patten, str);
		}

		/* (비Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DomainMatcher [");
			if (objectType != null)
				builder.append("objectType=").append(objectType).append(", ");
			if (objectId != null)
				builder.append("objectId=").append(objectId).append(", ");
			if (patten != null)
				builder.append("patten=").append(patten);
			builder.append("]");
			return builder.toString();
		}
		
}
