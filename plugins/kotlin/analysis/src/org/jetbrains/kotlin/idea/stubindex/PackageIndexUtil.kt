// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.stubindex

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile

object PackageIndexUtil {
    @JvmStatic
    fun getSubPackageFqNames(
        packageFqName: FqName,
        scope: GlobalSearchScope,
        project: Project,
        nameFilter: (Name) -> Boolean
    ): Collection<FqName> = SubpackagesIndexService.getInstance(project).getSubpackages(packageFqName, scope, nameFilter)

    @JvmStatic
    fun findFilesWithExactPackage(
        packageFqName: FqName,
        searchScope: GlobalSearchScope,
        project: Project
    ): Collection<KtFile> = KotlinExactPackagesIndex.getInstance().get(packageFqName.asString(), project, searchScope)

    @JvmStatic
    fun packageExists(
        packageFqName: FqName,
        searchScope: GlobalSearchScope,
        project: Project
    ): Boolean = SubpackagesIndexService.getInstance(project).packageExists(packageFqName, searchScope)
}
