package org.liamjd.spark.caisson.models

import org.liamjd.caisson.models.CaissonMultipartContent

data class SimpleFileUpload(val upload_description: String, val upload: CaissonMultipartContent)

data class MultipleFileUpload(val documents: List<CaissonMultipartContent>)